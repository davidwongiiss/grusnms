package com.grus.nms.daemon.monitor.nsg9000;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.w3c.dom.Document;

import com.grus.nms.daemon.monitor.nsg9000.pojo.EventValue;
import com.grus.nms.daemon.monitor.nsg9000.pojo.GbeValue;
import com.grus.nms.daemon.monitor.nsg9000.pojo.Node;
import com.grus.nms.daemon.monitor.nsg9000.pojo.NodeStatus;
import com.grus.nms.daemon.monitor.nsg9000.pojo.QamValue;
import com.grus.nms.daemon.monitor.util.XmlManager;

/**
 * 监视类
 * 
 * 以后拓展的话，可以在外部进行sender/receiver的配置，然后在run方法上进行handler的挂接（职责链模式）。
 * 
 * @author ipqam
 * 
 */
public class Monitor {
	static final int ALL = 0; // 貌似可以一次请求全部，需要验证测试
	static final int GBE = 1;
	static final int QAM = 2;
	static final int EVENT = 3;
	static final int STATUS = 4;

	public class ResponseQueueItem {
		public Node node;
		public int type;
		public String xml;

		ResponseQueueItem(Node node, int t, String x) {
			this.node = node;
			type = t;
			xml = x;
		}
	};

	public class DbQueueItem {
		public int type;
		public Object data;

		DbQueueItem(int t, Object data) {
			this.type = t;
			this.data = data;
		}
	};

	/**
	 * 每次连接，拉取gbe,qam,event
	 * 
	 * 如果效率有问题，可以分开拉取或者配置不同的时间间隔，但是要解决连接认证的效率问题
	 * 
	 * @author davidwongiiss
	 * 
	 */
	class DataPuller implements Runnable {
		private List<Node> nodes;
		private boolean stop = false;

		private Properties properties;

		private BlockingQueue<ResponseQueueItem> xmls;
		private BlockingQueue<DbQueueItem> dbObjects;		

		private int interval = 5000;

		/**
		 * 分配结点，多开网络连接线程
		 * 
		 * @param nodes
		 */
		public DataPuller(List<Node> node, BlockingQueue<ResponseQueueItem> xmls, BlockingQueue<DbQueueItem> dbObjects) {
			this.nodes = node;
			this.xmls = xmls;
			this.dbObjects = dbObjects;
		}

		public DataPuller(Node node, BlockingQueue<ResponseQueueItem> xmls, BlockingQueue<DbQueueItem> dbObjects) {
			this.nodes = new ArrayList<Node>();
			nodes.add(node);
			this.xmls = xmls;
			this.dbObjects = dbObjects;
		}
		
		public void run() {
			String ips = properties.getProperty("test.ips", "");
			int interval = Integer.parseInt(getProperties().getProperty("pull.interval", "5000"));
			int timeout = Integer.parseInt(getProperties().getProperty("pull.timeout", "5000"));
			
			while (!isStop()) {
				for (int i = 0; i < this.nodes.size() && !isStop(); i++) {
					Node node = nodes.get(i);
					
					if (!ips.isEmpty() && !ips.contains(node.getIp()))
						continue;

					NodeStatus ns = new NodeStatus();
					ns.node_id = node.getId();
					ns.ip = node.getIp();
					ns.name = node.getName();
					
					// 连接池，以后http连接认证和session在这里进行管理
					HttpConnection conn = HttpConnection.connect(node.getIp(), node.getLoginUser(), node.getLoginPassword(), timeout);
					if (conn == null) {
						ns.status = 1;
						try {
							this.dbObjects.put(new DbQueueItem(Monitor.STATUS, ns));
						}
						catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						continue;
					}
					
					ns.status = 0;
					try {
						this.dbObjects.put(new DbQueueItem(Monitor.STATUS, ns));
					}
					catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}					
					
					try {
						// 以后再有数据发送的话，提取为request对象链。
						String xml = "";

						/*
						if (Boolean.parseBoolean(getProperties().getProperty("pull.gbe.enabled", "true"))) {
							xml = conn.getGbeData();
							this.xmls.put(new ResponseQueueItem(node, Monitor.GBE, xml));
							System.out.println(xml);
						}

						// 发送0-9个slot
						if (Boolean.parseBoolean(getProperties().getProperty("pull.qam.enabled", "true"))) {
							for (int n = 0; n < 1; n++) {
								try {
									xml = conn.getQamData(n + 1);
									this.xmls.put(new ResponseQueueItem(node, Monitor.QAM, xml));
									System.out.println(xml);
								}
								catch (Exception e) {
									e.printStackTrace();
								}
							}
						}

						if (Boolean.parseBoolean(getProperties().getProperty("pull.event.enabled", "true"))) {
							xml = conn.getEvents();
							this.xmls.put(new ResponseQueueItem(node, Monitor.EVENT, xml));
							System.out.println(xml);
						}
						*/
						xml = conn.getAllData();
						if (!xml.isEmpty()) {
							System.out.println(xml);
							this.xmls.put(new ResponseQueueItem(node, Monitor.ALL, xml));
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					finally {
						// 可能未中断，只是归还连接池
						conn.disconnect();
					}

					Thread.yield();
				}

				try {
					Thread.sleep(interval);
				}
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public boolean isStop() {
			return stop;
		}

		public void setStop(boolean stop) {
			this.stop = stop;
		}

		public int getInterval() {
			return interval;
		}

		public void setInterval(int interval) {
			this.interval = interval;
		}

		public Properties getProperties() {
			return properties;
		}

		public void setProperties(Properties properties) {
			this.properties = properties;
		}
	}

	/**
	 * 充分利用cpu时间，实现xml分析的处理，少开线程
	 * 
	 * @author ipqam
	 * 
	 */
	class Parser implements Runnable {
		private boolean stop = false;

		private BlockingQueue<ResponseQueueItem> xmls;
		private BlockingQueue<DbQueueItem> objects;

		/**
		 * 
		 * @param nodes
		 */
		public Parser(BlockingQueue<ResponseQueueItem> xmls, BlockingQueue<DbQueueItem> objects) {
			this.xmls = xmls;
			this.objects = objects;
		}

		public void run() {
			while (!isStop()) {
				//while (!xmls.isEmpty() && !isStop()) {
					ResponseQueueItem item;
					try {
						item = xmls.take();
						Document document = XmlManager.parse(new ByteArrayInputStream(item.xml.getBytes()));

						// 此处以后可以改为handlers职责链，可以参照java netty nio库的设计模式
						Object object = null;
						if (item.type == Monitor.GBE) {
							object = XmlParser.gbeXmlParser(document, item.node);
							if (object != null)
								this.objects.put(new DbQueueItem(item.type, object));
						}
						else if (item.type == Monitor.QAM) {
							// object = XmlParser.qamXmlParser(item.xml, item.node);
							object = XmlParser.qamXmlParser1(document, item.node);
							if (object != null)
								this.objects.put(new DbQueueItem(item.type, object));
						}
						else if (item.type == Monitor.EVENT) {
							object = XmlParser.eventXmlParser(document, item.node);
							if (object != null)
								this.objects.put(new DbQueueItem(item.type, object));
						}
						else if (item.type == Monitor.ALL) {
							List<XmlParser.Result> objects = XmlParser.handleAll(document, item.node);
							Iterator<XmlParser.Result> itr = objects.iterator();
							while (itr.hasNext()) {
								XmlParser.Result nextObj = itr.next();
							   if (nextObj.type == XmlParser.Result.GBE) {
							  	 this.objects.put(new DbQueueItem(Monitor.GBE, nextObj.object));
							   }
							   else if (nextObj.type == XmlParser.Result.QAM) {
							  	 this.objects.put(new DbQueueItem(Monitor.QAM, nextObj.object));
							   }
							   else if (nextObj.type == XmlParser.Result.EVENT) {
							  	 this.objects.put(new DbQueueItem(Monitor.EVENT, nextObj.object));
							   }
							   else {
							  	 ;
							   }							   
							}							
						}

						Thread.yield();
					}
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				//}
				
				Thread.yield();
			}
		}

		public boolean isStop() {
			return stop;
		}

		public void setStop(boolean stop) {
			this.stop = stop;
		}
	}

	/**
	 * 根据实际数据库的负载情况或者队列数据情况，适当开入库线程
	 * 
	 * @author ipqam
	 * 
	 */
	class Storage implements Runnable {
		private BlockingQueue<DbQueueItem> objects;
		private boolean stop = false;

		private Connection conn;

		public Storage(Connection conn, BlockingQueue<DbQueueItem> objects) {
			this.conn = conn;
			this.objects = objects;
		}

		@SuppressWarnings("unchecked")
		public void run() {
			DbAccessor writer = new DbAccessor(this.conn);

			while (!isStop()) {	
				//while (!objects.isEmpty() && !isStop()) {
					DbQueueItem item;
					try {
						item = objects.take();

						if (item.type == Monitor.GBE) {
							writer.handleGbe((GbeValue) item.data);
						}
						else if (item.type == Monitor.QAM) {
							//List<QamValue> qams = new ArrayList<QamValue>();
							//qams.add((QamValue) item.data);
							writer.handleQam((List<QamValue>)item.data);
						}
						else if (item.type == Monitor.EVENT) {
							writer.handleEvent((List<EventValue>) item.data);
						}
						else if (item.type == Monitor.STATUS) {
							writer.handleStatus((NodeStatus) item.data);
						}
						
						Thread.yield();
					}
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				//}
				
				Thread.yield();
			}
		}

		public boolean isStop() {
			return stop;
		}

		public void setStop(boolean stop) {
			this.stop = stop;
		}
	}

	private Properties properties;
	private ExecutorService executorService;
	private List<DataPuller> dps = new ArrayList<DataPuller>();
	private List<Parser> parsers = new ArrayList<Parser>();
	private List<Storage> dbwriters = new ArrayList<Storage>();

	public void start() throws Exception {
		InputStream inStream = null;
		inStream = Monitor.class.getClassLoader().getResourceAsStream("conf/deamon.monitor.properties");
		if (inStream == null) {
			throw new Exception("dasdas");
		}

		this.properties = new Properties();
		try {
			this.properties.load(inStream);

			// 启动入库线程
			// Class.forName("com.mysql.jdbc.Driver");
			Class.forName(this.properties.getProperty("database.driver"));

			String connString = this.properties.getProperty("database.connectString");
			String user = this.properties.getProperty("database.username");
			String pwd = this.properties.getProperty("database.password");
			Connection conn = DriverManager.getConnection(connString, user, pwd);

			List<Node> nodes = DbAccessor.getAllNodes(conn);
			if (nodes.isEmpty()) {
				System.out.println("未发现设备结点");
				return;
			}

			// 获取http连接池树
			int per = Integer.parseInt(this.properties.getProperty("pull.queryNodeOfPerThread", "100"));
			int poolSize = nodes.size() / per + ((nodes.size() % per != 0) ? 1 : 0);			
			int cpuNums = Integer.parseInt(this.properties.getProperty("pull.parser.count", "1")); //Runtime.getRuntime().availableProcessors();
			int dbSize = Integer.parseInt(this.properties.getProperty("pull.dbaccessor.count", "2"));
			
			int tc = poolSize + cpuNums + dbSize;
			this.executorService = Executors.newFixedThreadPool(tc);

			LinkedBlockingQueue<ResponseQueueItem> xmlQueue = new LinkedBlockingQueue<ResponseQueueItem>();
			LinkedBlockingQueue<DbQueueItem> dbQueue = new LinkedBlockingQueue<DbQueueItem>();
			
			// 启动http数据抓取线程
			int i = 0;
			while (i < nodes.size()) {
				List<Node> sub = nodes.subList(i, Math.min(nodes.size(), i + per));
				i += per;

				DataPuller dp = new DataPuller(sub, xmlQueue, dbQueue);
				dp.setProperties(properties);
				this.dps.add(dp);
				this.executorService.execute(dp);
			}

			// 启动分析处理线程
			for (i = 0; i < cpuNums; i++) {
				Parser p = new Parser(xmlQueue, dbQueue);
				this.parsers.add(p);
				this.executorService.execute(p);
			}

			// 启动入库线程
			for (i = 0; i < dbSize; i++) {
				// 数据库连接池由JDBC管理。
				conn = DriverManager.getConnection(connString, user, pwd);
				conn.setAutoCommit(true);

				Storage s = new Storage(conn, dbQueue);
				this.dbwriters.add(s);
				this.executorService.execute(s);
			}

			this.executorService.shutdown();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stop() {
		// 全部设stop，其实用Monitor全局变量最简单，不用保存这么多实例，或者在start里用闭包。
		for (DataPuller p : this.dps) {
			p.setStop(true);
		}

		for (Parser p : this.parsers) {
			p.setStop(true);
		}

		for (Storage w : this.dbwriters) {
			w.setStop(true);
		}

		// 等待结束
		if (this.executorService != null)
			this.executorService.isTerminated();
	}

	/**
	 * 
	 * 增加新设备和修改，需要重新启动，没有事件通知。 后期可以在数据库或者外部文件加标志，进行轮询
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		Monitor m = new Monitor();
		try {
			if (args.length == 0) {
				m.stop();
				m.start();
			}
			else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("-start")) {
					m.start();
				}
				else if (args[0].equalsIgnoreCase("-stop")) {
					m.stop();
				}
				else if (args[0].equalsIgnoreCase("-restart")) {
					m.stop();
					m.start();
				}
			}
			else {
				System.out.println("参数错误！");
				return;
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}