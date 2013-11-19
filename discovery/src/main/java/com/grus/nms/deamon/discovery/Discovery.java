package com.grus.nms.deamon.discovery;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 * 使用广播的方式发送snmp，获取NSG9000的设备信息，发现是NSG9000的设备，
 * 将设备入库或者按mac地址更新ip
 * 
 * @author davidwongiiss
 *
 */
public class Discovery {
	// systemDescr
	static final String OID1 = "1.3.6.1.2.1.1.1.0";
	// eth0
	static final String OID2 = "1.3.6.1.2.1.2.2.1.2.2";
	// MAC
	static final String OID3 = "1.3.6.1.2.1.2.2.1.6.2";

	public class DeviceInfo {
		public String system;
		public String ip;
		public String mac;
	};

	/**
	 * 入设备数据库
	 * 
	 * @author davidwongiiss
	 * 
	 */
	class Consumer implements Runnable {
		private BlockingQueue<DeviceInfo> devices;
		private boolean stop = false;
		private Connection conn = null;

		/**
		 * @param devices
		 */
		public Consumer(BlockingQueue<DeviceInfo> devices, Connection conn) {
			this.devices = devices;
			this.conn = conn;
		}

		public void stop() {
			this.stop = true;
		}

		public void run() {
			// TODO Auto-generated method stub
			while (!this.stop) {
				Thread.yield();
				while (this.devices.size() != 0) {
					try {
						DeviceInfo info = this.devices.take();
						saveDb(info);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		private void saveDb(DeviceInfo info) {
			try {
				PreparedStatement ps;
				ps = this.conn.prepareStatement("SELECT COUNT(*) FROM grusbiz.nodes WHERE mac=?");

				ps.setString(1, info.mac);
				ResultSet rs = ps.executeQuery();
				rs.next();
				int count = rs.getInt(1);
				if (count == 0) { // 插入
					ps = this.conn
							.prepareStatement("INSERT INTO grusbiz.nodes(name, mac, ip) VALUES(?, ?, ?)");
					int i = 0;
					ps.setString(++i, info.ip + "-NSG9000 6G");
					ps.setString(++i, info.mac);
					ps.setString(++i, info.ip);							
				}
				else { // 新建
					ps = this.conn
							.prepareStatement("UPDATE grusbiz.nodes SET ip=? WHERE mac=?");
					int i = 0;
					ps.setString(++i, info.ip);
					ps.setString(++i, info.mac);
				}
		
				ps.executeUpdate();
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发送snmp，发现设备，为了更好的交互，使用异步方式进行广播 SNMPv1/v2c
	 * 
	 * @author davidwongiiss
	 * 
	 */
	class Producer implements Runnable {
		private Snmp snmp = null;
		private BlockingQueue<DeviceInfo> devices = null;

		public Producer(BlockingQueue<DeviceInfo> devices) {
			this.devices = devices;
		}

		public void stop() {
			try {
				if (snmp != null) {
					snmp.close();
				}
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * 在run里设置广播
		 */
		public void run() {
			@SuppressWarnings("rawtypes")
			TransportMapping transport = null;
			try {
				transport = new DefaultUdpTransportMapping();
			}
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			PDU pdu = new PDU();

			pdu.add(new VariableBinding(new OID(OID1))); // sysDescr
			pdu.add(new VariableBinding(new OID(OID2))); // eth0
			pdu.add(new VariableBinding(new OID(OID3))); // mac
			pdu.setType(PDU.GET);

			this.snmp = new Snmp(transport);
			try {
				this.snmp.listen();
			}
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} // TODO Auto-generated method stub

			ResponseListener listener = new ResponseListener() {
				public void onResponse(ResponseEvent event) {
					// Always cancel async request when response has been
					// received
					// otherwise a memory leak is created! Not canceling a
					// request
					// immediately can be useful when sending a request to a
					// broadcast
					// address.
					((Snmp) event.getSource()).cancel(event.getRequest(), this);
					PDU response = event.getResponse();
					PDU request = event.getRequest();
					if (response == null) {
						System.out.println("Request " + request + " timed out");
					}
					else {
						System.out.println("Received response from: "
								+ event.getPeerAddress());

						@SuppressWarnings("unchecked")
						Vector<VariableBinding> vs = (Vector<VariableBinding>) response
								.getVariableBindings();
						VariableBinding vb = vs.elementAt(0);
						Variable v = vb.getVariable();
						String sysDesc = v.toString();
						if (sysDesc.contains("Harmonic Inc")
								&& sysDesc.contains("EQAM")
								&& sysDesc.contains("9000")) {
							DeviceInfo info = new DeviceInfo();
							info.system = sysDesc;

							vb = vs.elementAt(1);
							v = vb.getVariable();

							Address addr = event.getPeerAddress();
							String ip = addr.toString().split("/")[0];
							info.ip = ip;

							vb = vs.elementAt(2);
							v = vb.getVariable();
							info.mac = v.toString();

							try {
								devices.put(info);
							}
							catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						System.out.println("Received response " + response
								+ " on request " + request);
					}
				}
			};

			CommunityTarget target = new CommunityTarget();
			target.setCommunity(new OctetString("public"));
			target.setAddress(GenericAddress.parse("udp:255.255.255.255/161"));
			target.setRetries(2);
			target.setTimeout(5000);
			target.setVersion(SnmpConstants.version1);

			try {
				this.snmp.send(pdu, target, null, listener);
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	private Producer p = null;
	private Consumer c = null;
	private Thread thread1 = null;
	private Thread thread2 = null;
	private Connection conn = null;
	private Properties props = null;
	
	public Discovery(Properties props) {
		this.props = props;		
	}

	public void start() {
		BlockingQueue<DeviceInfo> devices = new LinkedBlockingQueue<DeviceInfo>();
		this.p = new Producer(devices);
		
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			Class.forName(props.getProperty("database.driver"));
		}
		catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			String connString = props.getProperty("database.connectString");
			String u = props.getProperty("database.username");
			String p = props.getProperty("database.password");
			
			//this.conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orc8i", "java", "88888888");
			this.conn = DriverManager.getConnection(connString, u, p);
			this.conn.setAutoCommit(true);
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.c = new Consumer(devices, this.conn);

		this.thread1 = new Thread(this.p);
		this.thread2 = new Thread(this.c);

		this.thread1.start();
		this.thread2.start();
	}

	/**
	 * 
	 */
	public void stop() {
		if (this.p != null) {
			this.p.stop();
			try {
				this.thread1.join();
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (this.c != null) {
			this.c.stop();
			try {
				this.thread2.join();
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (this.conn != null) {
			try {
				this.conn.close();
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return;
	}
	
	public static void main(String args[]) {
		InputStream inStream = null;
		
		inStream = Discovery.class.getClassLoader().getResourceAsStream("conf/deamon.discovery.properties");
		
		Properties props = new Properties();
		try {
			props.load(inStream);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		Discovery d = new Discovery(props);
		
		if (args.length == 0) {
			d.stop();
			d.start();
		}
		else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("-start")) {
				d.start();
			}
			else if (args[0].equalsIgnoreCase("-stop")) {
				d.stop();
			}
			else if (args[0].equalsIgnoreCase("-restart")) {
				d.stop();
				d.start();
			}
		}
		else {
			System.out.println("参数错误！");
			return;
		}
	}
}

