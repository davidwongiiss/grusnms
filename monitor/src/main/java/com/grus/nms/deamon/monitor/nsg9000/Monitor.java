package com.grus.nms.deamon.monitor.nsg9000;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.grus.nms.deamon.monitor.pojo.EventValue;
import com.grus.nms.deamon.monitor.pojo.GbeValue;
import com.grus.nms.deamon.monitor.pojo.Node;
import com.grus.nms.deamon.monitor.pojo.QamValue;

/**
 * ������
 * 
 * �Ժ���չ�Ļ����������ⲿ����sender/receiver�����ã�Ȼ����run�����Ͻ���handler�Ĺҽӣ�ְ����ģʽ����
 * 
 * @author ipqam
 * 
 */
public class Monitor {
	static final int ALL = 0; // ò�ƿ���һ������ȫ������Ҫ��֤����
	static final int GBE = 1;
	static final int QAM = 2;
	static final int EVENT = 3;

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
	 * ÿ�����ӣ���ȡgbe,qam,event
	 * 
	 * ���Ч�������⣬���Էֿ���ȡ�������ò�ͬ��ʱ����������Ҫ���������֤��Ч������
	 * 
	 * @author davidwongiiss
	 * 
	 */
	class DataPuller implements Runnable {
		private List<Node> nodes;
		private boolean stop = false;

		private BlockingQueue<ResponseQueueItem> xmls;

		private int interval = 0;

		/**
		 * �����㣬�࿪���������߳�
		 * 
		 * @param nodes
		 */
		public DataPuller(List<Node> node, BlockingQueue<ResponseQueueItem> xmls) {
			this.xmls = xmls;
		}

		public DataPuller(Node node, BlockingQueue<ResponseQueueItem> xmls) {
			this.nodes = new ArrayList<Node>();
			nodes.add(node);
			this.xmls = xmls;
		}

		public void run() {
			while (!isStop()) {
				for (int i = 0; i < this.nodes.size() && !isStop(); i++) {
					Node node = nodes.get(i);

					// ���ӳأ��Ժ�http������֤��session��������й���
					HttpConnection conn = HttpConnection.connect(node.getIp(), node.getLoginUser(), node.getLoginPassword());
					try {
						// �Ժ��������ݷ��͵Ļ�����ȡΪrequest��������
						String xml = conn.getGbeData();
						this.xmls.put(new ResponseQueueItem(node, Monitor.GBE, xml));

						xml = conn.getQamData();
						this.xmls.put(new ResponseQueueItem(node, Monitor.QAM, xml));

						xml = conn.getEvents();
						this.xmls.put(new ResponseQueueItem(node, Monitor.EVENT, xml));
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					finally {
						// ����δ�жϣ�ֻ�ǹ黹���ӳ�
						conn.disconnect();
					}

					Thread.yield();
				}

				try {
					Thread.sleep(this.getInterval());
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
	}

	/**
	 * �������cpuʱ�䣬ʵ��xml�����Ĵ����ٿ��߳�
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
				while (!xmls.isEmpty() && !isStop()) {
					ResponseQueueItem item;
					try {
						item = xmls.take();

						// �˴��Ժ���Ը�Ϊhandlersְ���������Բ���java netty nio������ģʽ
						Object object = null;
						if (item.type == Monitor.GBE) {
							object = XmlParser.gbeXmlParser(item.xml, item.node);
						}
						else if (item.type == Monitor.QAM) {
							object = XmlParser.qamXmlParser(item.xml, item.node);
						}
						else if (item.type == Monitor.EVENT) {
							object = XmlParser.eventXmlParser(item.xml, item.node);
						}

						if (object != null)
							this.objects.put(new DbQueueItem(item.type, object));

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
				}
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
	 * ����ʵ�����ݿ�ĸ���������߶�������������ʵ�������߳�
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
			DbWriter writer = new DbWriter(this.conn);

			while (!isStop()) {
				while (!objects.isEmpty() && !isStop()) {
					DbQueueItem item;
					try {
						item = objects.take();

						if (item.type == Monitor.GBE) {
							writer.handleGbe((GbeValue) item.data);
						}
						else if (item.type == Monitor.QAM) {
							writer.handleQam((List<QamValue>) item.data);
						}
						else if (item.type == Monitor.EVENT) {
							writer.handleEvent((List<EventValue>) item.data);
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
				}
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
	private ExecutorService executorService1;
	private ExecutorService executorService2;
	private ExecutorService executorService3;
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

			// ��ȡhttp���ӳ���
			int per = Integer.parseInt(this.properties.getProperty("queryNodeOfPerThread"));
			List<Node> nodes = new ArrayList<Node>();
			int poolSize = nodes.size() / per + ((nodes.size() % per != 0) ? 1 : 0);

			// ����http����ץȡ�߳�
			LinkedBlockingQueue<ResponseQueueItem> xmlQueue = new LinkedBlockingQueue<ResponseQueueItem>();
			this.executorService1 = Executors.newFixedThreadPool(poolSize);

			int i = 0;
			while (i < nodes.size()) {
				List<Node> sub = nodes.subList(i, i + per);
				i += per;

				DataPuller dp = new DataPuller(sub, xmlQueue);
				this.dps.add(dp);
				this.executorService1.execute(dp);
			}

			this.executorService1.shutdown();

			// �������������߳�
			LinkedBlockingQueue<DbQueueItem> dbQueue = new LinkedBlockingQueue<DbQueueItem>();

			int cpuNums = Runtime.getRuntime().availableProcessors();
			this.executorService2 = Executors.newFixedThreadPool(cpuNums);
			for (i = 0; i < cpuNums; i++) {
				Parser p = new Parser(xmlQueue, dbQueue);
				this.parsers.add(p);
				this.executorService2.execute(p);
			}

			this.executorService2.shutdown();

			// ��������߳�
			// Class.forName("com.mysql.jdbc.Driver");
			Class.forName(this.properties.getProperty("database.driver"));

			String connString = this.properties.getProperty("database.connectString");
			String u = this.properties.getProperty("database.username");
			String p = this.properties.getProperty("database.password");

			this.executorService3 = Executors.newFixedThreadPool(poolSize);
			for (i = 0; i < poolSize; i++) {
				// ���ݿ����ӳ���JDBC����
				// this.conn =
				// DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orc8i",
				// "java", "88888888");
				Connection conn = DriverManager.getConnection(connString, u, p);
				conn.setAutoCommit(true);

				Storage s = new Storage(conn, dbQueue);
				this.dbwriters.add(s);
				this.executorService3.execute(s);
			}

			this.executorService3.shutdown();
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
		// ȫ����stop����ʵ��Monitorȫ�ֱ�����򵥣����ñ�����ô��ʵ����������start���ñհ���
		for (DataPuller p : this.dps) {
			p.setStop(true);
		}

		for (Parser p : this.parsers) {
			p.setStop(true);
		}

		for (Storage w : this.dbwriters) {
			w.setStop(true);
		}

		// �ȴ�����
		if (this.executorService1 != null)
			this.executorService1.isTerminated();

		if (this.executorService2 != null)
			this.executorService2.isTerminated();

		if (this.executorService3 != null)
			this.executorService3.isTerminated();
	}

	/**
	 * 
	 * �������豸���޸ģ���Ҫ����������û���¼�֪ͨ�� ���ڿ��������ݿ�����ⲿ�ļ��ӱ�־��������ѯ
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
				System.out.println("��������");
				return;
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}