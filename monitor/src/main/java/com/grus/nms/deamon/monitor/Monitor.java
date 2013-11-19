package com.grus.nms.deamon.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;

import com.grus.nms.deamon.monitor.dbc.DatabaseConnection;
import com.grus.nms.deamon.monitor.factory.ServicesFactory;
import com.grus.nms.deamon.monitor.pojo.EventValue;
import com.grus.nms.deamon.monitor.pojo.GbeValue;
import com.grus.nms.deamon.monitor.pojo.Node;
import com.grus.nms.deamon.monitor.pojo.QamValue;
import com.grus.nms.deamon.monitor.services.EventWriter;

/**
 * 监视类
 * 
 * 以后拓展的话，可以在外部进行sender/receiver的配置，然后在run方法上进行handler的挂接（职责链模式）。
 * 
 * @author ipqam
 *
 */
public class Monitor {
	public class QueueItem {
		static final int GBE = 0;
		static final int QAM = 1;
		static final int EVENT = 2;

		public Node node;
		public int type;
		public String xml;

		QueueItem(Node node, int t, String x) {
			this.node = node;
			type = t;
			xml = x;
		}
	};

	/**
	 * 每次连接，拉取gbe,qam,event
	 * 
	 * 如果效率有问题，可以分开拉取或者配置不同的时间间隔，但是要解决连接认证的效率问题，既保持连接的session
	 * 
	 * @author davidwongiiss
	 * 
	 */
	class DataPuller implements Runnable {
		private List<Node> nodes;
		private boolean stop = false;

		private BlockingQueue<QueueItem> xmls;

		private int interval = 5000;

		/**
		 * 分配结点，多开网络连接线程
		 * 
		 * @param nodes
		 */
		public DataPuller(List<Node> node, BlockingQueue<QueueItem> xmls) {
			this.xmls = xmls;
		}

		public void run() {
			Thread t = Thread.currentThread();
			while (!isStop()) {
				for (int i = 0; i < this.nodes.size() && !isStop(); i++) {
					Node node = nodes.get(i);

					// 连接池，以后http连接认证和session在这里进行管理
					DeviceConnection conn = DeviceConnection.connect(node);
					try {
						String xml = conn.getGbeData();
						this.xmls.put(new QueueItem(node, QueueItem.GBE, xml));

						xml = conn.getQamData();
						this.xmls.put(new QueueItem(node, QueueItem.QAM, xml));

						xml = conn.getEvents();
						this.xmls.put(new QueueItem(node, QueueItem.EVENT, xml));
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					finally {
						// 可能未中断，只是归还连接池
						conn.disconnect();
					}
				}

				Thread.sleep(this.getInterval());
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
	 * 充分利用cpu时间，实现xml分析的处理，少开线程
	 * 
	 * @author ipqam
	 * 
	 */
	class Parser implements Runnable {
		private boolean stop = false;

		private BlockingQueue<QueueItem> xmls;
		private BlockingQueue<Object> objects;

		/**
		 * 
		 * @param nodes
		 */
		public Parser(BlockingQueue<QueueItem> xmls, BlockingQueue<Object> objects) {
			this.xmls = xmls;
			this.objects = objects;
		}

		public void run() {
			while (!isStop()) {
				while (!xmls.isEmpty() && !isStop()) {
					QueueItem item;
					try {
						item = xmls.take();

						Object object = null;
						if (item.type == QueueItem.GBE) {

						}
						else if (item.type == QueueItem.QAM) {

						}
						else if (item.type == QueueItem.EVENT) {

						}

						if (object != null)
							this.objects.put(object);
					}
					catch (InterruptedException e) {
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
	 * 根据实际数据库的负载情况或者队列数据情况，适当开入库线程
	 * 
	 * @author ipqam
	 * 
	 */
	class Storage implements Runnable {
		private BlockingQueue<Object> objects;
		private boolean stop = false;
		
		private DatabaseConnection conn;

		public Storage(DatabaseConnection conn, BlockingQueue<Object> objects) {
			this.conn = conn;
			this.objects = objects;
		}

		public void run() {
			while (!isStop()) {
				while (!objects.isEmpty() && !isStop()) {
					Object object;
					try {
						object = objects.take();

						if (object instanceof GbeValue) {
							ServicesFactory.getIGbeValuesServicesInstance(conn).insert((GbeValue)object);
						}
						else if (object instanceof QamValue) {
							ServicesFactory.getIQamValuesServicesInstance(conn).insert((QamValue)object);
						}
						else if (object instanceof EventValue) {
							EventWriter writer = new EventWriter(this.conn);
							writer.handle((EventValue)object);
						}
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

	public void start() {
		InputStream inStream = null;
		inStream = Monitor.class.getClassLoader().getResourceAsStream(
				"conf/deamon.monitor.properties");

		this.properties = new Properties();
		try {
			this.properties.load(inStream);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	public void stop() {
	}

	/**
	 * 
	 * 增加新设备和修改，需要重新启动，没有事件通知。
	 * 后期可以在数据库或者外部文件加标志，进行轮询
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		Monitor m = new Monitor();

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
}
