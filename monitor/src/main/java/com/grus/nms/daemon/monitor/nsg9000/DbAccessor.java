package com.grus.nms.daemon.monitor.nsg9000;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.grus.nms.daemon.monitor.nsg9000.pojo.EventValue;
import com.grus.nms.daemon.monitor.nsg9000.pojo.GbeValue;
import com.grus.nms.daemon.monitor.nsg9000.pojo.Node;
import com.grus.nms.daemon.monitor.nsg9000.pojo.QamValue;

public class DbAccessor {
	private Connection conn;

	public DbAccessor(Connection conn) {
		this.conn = conn;
	}

	/**
	 * 加入gbe
	 * 
	 * @param vals
	 */
	public void handleGbe(GbeValue gbe) {
		boolean autoCommit = false;
		try {
			autoCommit = this.conn.getAutoCommit();
		}
		catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			this.conn.setAutoCommit(false);

			String sql = "SELECT node_id FROM grusnms.gbe_cur_values WHERE node_id = ? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, gbe.getNodeId());
			ResultSet rs = pstmt.executeQuery();

			boolean exists = rs.next();
			String sqls[] = { "", "" };
			if (exists) {
				sqls[0] = "UPDATE grusnms.gbe_cur_values SET numofservices1=?,numofservices2=?,numofservices3=?,numofservices4=?,numofservices5=?,numofservices6=?,numofservices7=?,numofservices8=?,"
						+ " multicastbitrate1=?,multicastbitrate2=?,multicastbitrate3=?,multicastbitrate4=?,multicastbitrate5=?,multicastbitrate6=?,multicastbitrate7=?,multicastbitrate8=?,create_time=?"
						+ " WHERE node_id = ? ";
			}
			else {
				sqls[0] = "INSERT INTO grusnms.gbe_cur_values(numofservices1,numofservices2,numofservices3,numofservices4,numofservices5,numofservices6,numofservices7,numofservices8,"
						+ " multicastbitrate1,multicastbitrate2,multicastbitrate3,multicastbitrate4,multicastbitrate5,multicastbitrate6,multicastbitrate7,multicastbitrate8,create_time,node_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			}

			sqls[1] = "INSERT INTO grusnms.gbe_day_values(numofservices1,numofservices2,numofservices3,numofservices4,numofservices5,numofservices6,numofservices7,numofservices8,"
					+ " multicastbitrate1,multicastbitrate2,multicastbitrate3,multicastbitrate4,multicastbitrate5,multicastbitrate6,multicastbitrate7,multicastbitrate8,create_time,node_id,id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

			for (int m = 0; m < 2; m++) {
				try {
					pstmt = conn.prepareStatement(sqls[m]);
					int i = 0;
					pstmt.setLong(++i, gbe.getNumberOfServices1());
					pstmt.setLong(++i, gbe.getNumberOfServices2());
					pstmt.setLong(++i, gbe.getNumberOfServices3());
					pstmt.setLong(++i, gbe.getNumberOfServices4());
					pstmt.setLong(++i, gbe.getNumberOfServices5());
					pstmt.setLong(++i, gbe.getNumberOfServices6());
					pstmt.setLong(++i, gbe.getNumberOfServices7());
					pstmt.setLong(++i, gbe.getNumberOfServices8());
					pstmt.setLong(++i, gbe.getMulticastBitrate1());
					pstmt.setLong(++i, gbe.getMulticastBitrate2());
					pstmt.setLong(++i, gbe.getMulticastBitrate3());
					pstmt.setLong(++i, gbe.getMulticastBitrate4());
					pstmt.setLong(++i, gbe.getMulticastBitrate5());
					pstmt.setLong(++i, gbe.getMulticastBitrate6());
					pstmt.setLong(++i, gbe.getMulticastBitrate7());
					pstmt.setLong(++i, gbe.getMulticastBitrate8());
					pstmt.setTimestamp(++i, gbe.getCreateTime());
					pstmt.setString(++i, gbe.getNodeId());
					
					if (m == 1) {
						String id = "";
						pstmt.setString(++i, id);
					}
					
					pstmt.executeUpdate();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}

			this.conn.commit();
		}
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			try {
				this.conn.rollback();
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		finally {
			try {
				this.conn.setAutoCommit(autoCommit);
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 加入qam
	 * 
	 * @param vals
	 */
	public void handleQam(List<QamValue> vals) {
		boolean autoCommit = false;
		try {
			autoCommit = this.conn.getAutoCommit();
		}
		catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			this.conn.setAutoCommit(false);

			for (QamValue qam : vals) {
				String sql = "SELECT node_id FROM grusnms.qam_cur_values WHERE node_id = ? ";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, qam.getNodeId());
				ResultSet rs = pstmt.executeQuery();

				boolean exists = rs.next();
				String sqls[] = { "", "" };
				if (exists) {
					sqls[0] = "UPDATE grusnms.qam_cur_values SET qam1=?,qam2=?,qam3=?,qam4=?,qam5=?,qam6=?,qam7=?,qam8=?,qam9=?,qam10=?,qam11=?,qam12=?,qam13=?,qam14=?,qam15=?,qam16=?,"
							+ " bitrate1=?,bitrate2=?,bitrate3=?,bitrate4=?,bitrate5=?,bitrate6=?,bitrate7=?,bitrate8=?,bitrate9=?,bitrate10=?,bitrate11=?,bitrate12=?,bitrate13=?,bitrate14=?,bitrate15=?,bitrate16=?,"
							+ " numofservices1=?,numofservices2=?,numofservices3=?,numofservices4=?,numofservices5=?,numofservices6=?,numofservices7=?,numofservices8=?,numofservices9=?,numofservices10=?,numofservices11=?,"
							+ " numofservices12=?,numofservices13=?,numofservices14=?,numofservices15=?,numofservices16=?,create_time=?,blade=? WHERE node_id = ? ";
				}
				else {
					sqls[0] = "INSERT INTO grusnms.qam_cur_values(qam1,qam2,qam3,qam4,qam5,qam6,qam7,qam8,qam9,qam10,qam11,qam12,qam13,qam14,qam15,qam16,"
							+ " bitrate1,bitrate2,bitrate3,bitrate4,bitrate5,bitrate6,bitrate7,bitrate8,bitrate9,bitrate10,bitrate11,bitrate12,bitrate13,bitrate14,bitrate15,bitrate16,"
							+ " numofservices1,numofservices2,numofservices3,numofservices4,numofservices5,numofservices6,numofservices7,numofservices8,numofservices9,numofservices10,numofservices11,"
							+ " numofservices12,numofservices13,numofservices14,numofservices15,numofservices16,create_time,blade,node_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
				}

				sqls[1] = "INSERT INTO grusnms.qam_day_values(qam1,qam2,qam3,qam4,qam5,qam6,qam7,qam8,qam9,qam10,qam11,qam12,qam13,qam14,qam15,qam16,"
						+ " bitrate1,bitrate2,bitrate3,bitrate4,bitrate5,bitrate6,bitrate7,bitrate8,bitrate9,bitrate10,bitrate11,bitrate12,bitrate13,bitrate14,bitrate15,bitrate16,"
						+ " numofservices1,numofservices2,numofservices3,numofservices4,numofservices5,numofservices6,numofservices7,numofservices8,numofservices9,numofservices10,numofservices11,"
						+ " numofservices12,numofservices13,numofservices14,numofservices15,numofservices16,create_time,blade,node_id,id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

				for (int m = 0; m < 2; m++) {
					try {
						int i = 0;
						pstmt = conn.prepareStatement(sql);
						pstmt.setBoolean(++i, qam.getQam1());
						pstmt.setBoolean(++i, qam.getQam2());
						pstmt.setBoolean(++i, qam.getQam3());
						pstmt.setBoolean(++i, qam.getQam4());
						pstmt.setBoolean(++i, qam.getQam5());
						pstmt.setBoolean(++i, qam.getQam6());
						pstmt.setBoolean(++i, qam.getQam7());
						pstmt.setBoolean(++i, qam.getQam8());
						pstmt.setBoolean(++i, qam.getQam9());
						pstmt.setBoolean(++i, qam.getQam10());
						pstmt.setBoolean(++i, qam.getQam11());
						pstmt.setBoolean(++i, qam.getQam12());
						pstmt.setBoolean(++i, qam.getQam13());
						pstmt.setBoolean(++i, qam.getQam14());
						pstmt.setBoolean(++i, qam.getQam15());
						pstmt.setBoolean(++i, qam.getQam16());

						pstmt.setLong(++i, qam.getBitrate1());
						pstmt.setLong(++i, qam.getBitrate2());
						pstmt.setLong(++i, qam.getBitrate3());
						pstmt.setLong(++i, qam.getBitrate4());
						pstmt.setLong(++i, qam.getBitrate5());
						pstmt.setLong(++i, qam.getBitrate6());
						pstmt.setLong(++i, qam.getBitrate7());
						pstmt.setLong(++i, qam.getBitrate8());
						pstmt.setLong(++i, qam.getBitrate9());
						pstmt.setLong(++i, qam.getBitrate10());
						pstmt.setLong(++i, qam.getBitrate11());
						pstmt.setLong(++i, qam.getBitrate12());
						pstmt.setLong(++i, qam.getBitrate13());
						pstmt.setLong(++i, qam.getBitrate14());
						pstmt.setLong(++i, qam.getBitrate15());
						pstmt.setLong(++i, qam.getBitrate16());

						pstmt.setLong(++i, qam.getNumOfServices1());
						pstmt.setLong(++i, qam.getNumOfServices2());
						pstmt.setLong(++i, qam.getNumOfServices3());
						pstmt.setLong(++i, qam.getNumOfServices4());
						pstmt.setLong(++i, qam.getNumOfServices5());
						pstmt.setLong(++i, qam.getNumOfServices6());
						pstmt.setLong(++i, qam.getNumOfServices7());
						pstmt.setLong(++i, qam.getNumOfServices8());
						pstmt.setLong(++i, qam.getNumOfServices9());
						pstmt.setLong(++i, qam.getNumOfServices10());
						pstmt.setLong(++i, qam.getNumOfServices11());
						pstmt.setLong(++i, qam.getNumOfServices12());
						pstmt.setLong(++i, qam.getNumOfServices13());
						pstmt.setLong(++i, qam.getNumOfServices14());
						pstmt.setLong(++i, qam.getNumOfServices15());
						pstmt.setLong(++i, qam.getNumOfServices16());
						pstmt.setTimestamp(++i, qam.getCreateTime());
						pstmt.setInt(++i, qam.getBlade());
						pstmt.setString(++i, qam.getNodeId());
						
						if (m == 1) {
							String id = "";
							pstmt.setString(++i, id);
						}

						pstmt.executeUpdate();
					}
					catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}

			this.conn.commit();
		}
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			try {
				this.conn.rollback();
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		finally {
			try {
				this.conn.setAutoCommit(autoCommit);
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 插入事件
	 * 
	 * @param events
	 */
	public void handleEvent(List<EventValue> events) {
		boolean autoCommit = false;
		try {
			autoCommit = this.conn.getAutoCommit();
		}
		catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			this.conn.setAutoCommit(false);

			for (EventValue event : events) {
				try {
					String sql = "SELECT id FROM grusnms.node_event WHERE node_id = ? AND seq_no = ? AND event_object = ? ";
					PreparedStatement pstmt = this.conn.prepareStatement(sql);
					pstmt.setString(1, event.getNodeId());
					pstmt.setString(2, event.getSeqNo());
					pstmt.setString(3, event.getEventObject());
					ResultSet rs = pstmt.executeQuery();
					if (rs.next()) {
						continue;
					}

					pstmt = conn.prepareStatement(sql);

					sql = "INSERT INTO grusnms.node_event(id,seq_no,event_id,event_object,description,severity,create_time,handled,event_time,t_user,node_id) "
							+ "VALUES(?,?,?,?,?,?,?,?,?,?,?) ";
					pstmt = conn.prepareStatement(sql);
					
					int i = 0;
					
					String id = "";
					pstmt.setString(++i, id);
					pstmt.setString(++i, event.getSeqNo());
					pstmt.setString(++i, event.getEventId());
					pstmt.setString(++i, event.getEventObject());
					pstmt.setString(++i, event.getDescription());
					pstmt.setString(++i, event.getSeverity());
					pstmt.setTimestamp(++i, event.getCreateTime());
					pstmt.setBoolean(++i, event.isHandled());
					pstmt.setTimestamp(++i, event.getEventTime());
					pstmt.setString(++i, event.getUser());
					pstmt.setString(++i, event.getNodeId());
					pstmt.executeUpdate();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}

			this.conn.commit();
		}
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			try {
				this.conn.rollback();
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		finally {
			try {
				this.conn.setAutoCommit(autoCommit);
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * get nodes
	 * @param conn
	 * @return
	 */
	public static List<Node> getAllNodes(Connection conn) {
		List<Node> list = new ArrayList<Node>();
		String sql = "SELECT id, ip, login_user, login_password FROM grusnms.nodes WHERE deleted != '1'";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);

			ResultSet rs = pstmt.executeQuery();
			Node node = null;
			while (rs.next()) {
				node = new Node();
				int i = 0;
				node.setId(rs.getString(++i));
				node.setIp(rs.getString(++i));
				node.setLoginUser(rs.getString(++i));
				node.setLoginPassword(rs.getString(++i));
				list.add(node);
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
}
