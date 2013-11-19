package com.grus.nms.deamon.monitor.nsg9000;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.grus.nms.deamon.monitor.pojo.EventValue;
import com.grus.nms.deamon.monitor.pojo.GbeValue;
import com.grus.nms.deamon.monitor.pojo.QamValue;

public class DbWriter {
	private Connection conn;

	public DbWriter(Connection conn) {
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
					+ " multicastbitrate1,multicastbitrate2,multicastbitrate3,multicastbitrate4,multicastbitrate5,multicastbitrate6,multicastbitrate7,multicastbitrate8,create_time,node_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

			for (int m = 0; m < 2; m++) {
				try {
					pstmt = conn.prepareStatement(sqls[m]);
					int i = 0;
					pstmt.setString(++i, gbe.getNodeId());
					pstmt.setInt(++i, gbe.getNumberOfServices1());
					pstmt.setInt(++i, gbe.getNumberOfServices2());
					pstmt.setInt(++i, gbe.getNumberOfServices3());
					pstmt.setInt(++i, gbe.getNumberOfServices4());
					pstmt.setInt(++i, gbe.getNumberOfServices5());
					pstmt.setInt(++i, gbe.getNumberOfServices6());
					pstmt.setInt(++i, gbe.getNumberOfServices7());
					pstmt.setInt(++i, gbe.getNumberOfServices8());
					pstmt.setInt(++i, gbe.getMulticastBitrate1());
					pstmt.setInt(++i, gbe.getMulticastBitrate2());
					pstmt.setInt(++i, gbe.getMulticastBitrate3());
					pstmt.setInt(++i, gbe.getMulticastBitrate4());
					pstmt.setInt(++i, gbe.getMulticastBitrate5());
					pstmt.setInt(++i, gbe.getMulticastBitrate6());
					pstmt.setInt(++i, gbe.getMulticastBitrate7());
					pstmt.setInt(++i, gbe.getMulticastBitrate8());
					pstmt.setTimestamp(++i, gbe.getCreateTime());
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
							+ " numofservices12=?,numofservices13=?,numofservices14=?,numofservices15=?,numofservices16=?,create_time=? WHERE node_id = ? ";
				}
				else {
					sqls[0] = "INSERT INTO grusnms.qam_cur_values(qam1,qam2,qam3,qam4,qam5,qam6,qam7,qam8,qam9,qam10,qam11,qam12,qam13,qam14,qam15,qam16," +
					     " bitrate1,bitrate2,bitrate3,bitrate4,bitrate5,bitrate6,bitrate7,bitrate8,bitrate9,bitrate10,bitrate11,bitrate12,bitrate13,bitrate14,bitrate15,bitrate16," +
					     " numofservices1,numofservices2,numofservices3,numofservices4,numofservices5,numofservices6,numofservices7,numofservices8,numofservices9,numofservices10,numofservices11," +
					     " numofservices12,numofservices13,numofservices14,numofservices15,numofservices16,create_time,blade,node_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
				}

				sqls[1] = "INSERT INTO grusnms.qam_day_values(qam1,qam2,qam3,qam4,qam5,qam6,qam7,qam8,qam9,qam10,qam11,qam12,qam13,qam14,qam15,qam16," +
				     " bitrate1,bitrate2,bitrate3,bitrate4,bitrate5,bitrate6,bitrate7,bitrate8,bitrate9,bitrate10,bitrate11,bitrate12,bitrate13,bitrate14,bitrate15,bitrate16," +
				     " numofservices1,numofservices2,numofservices3,numofservices4,numofservices5,numofservices6,numofservices7,numofservices8,numofservices9,numofservices10,numofservices11," +
				     " numofservices12,numofservices13,numofservices14,numofservices15,numofservices16,create_time,blade,node_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

				for (int m = 0; m < 2; m++) {
					try {
						int i = 0;
						pstmt = conn.prepareStatement(sql);
						pstmt.setInt(++i, qam.getQam1());
						pstmt.setInt(++i, qam.getQam2());
						pstmt.setInt(++i, qam.getQam3());
						pstmt.setInt(++i, qam.getQam4());
						pstmt.setInt(++i, qam.getQam5());
						pstmt.setInt(++i, qam.getQam6());
						pstmt.setInt(++i, qam.getQam7());
						pstmt.setInt(++i, qam.getQam8());
						pstmt.setInt(++i, qam.getQam9());
						pstmt.setInt(++i, qam.getQam10());
						pstmt.setInt(++i, qam.getQam11());
						pstmt.setInt(++i, qam.getQam12());
						pstmt.setInt(++i, qam.getQam13());
						pstmt.setInt(++i, qam.getQam14());
						pstmt.setInt(++i, qam.getQam15());
						pstmt.setInt(++i, qam.getQam16());

						pstmt.setInt(++i, qam.getBitrate1());
						pstmt.setInt(++i, qam.getBitrate2());
						pstmt.setInt(++i, qam.getBitrate3());
						pstmt.setInt(++i, qam.getBitrate4());
						pstmt.setInt(++i, qam.getBitrate5());
						pstmt.setInt(++i, qam.getBitrate6());
						pstmt.setInt(++i, qam.getBitrate7());
						pstmt.setInt(++i, qam.getBitrate8());
						pstmt.setInt(++i, qam.getBitrate9());
						pstmt.setInt(++i, qam.getBitrate10());
						pstmt.setInt(++i, qam.getBitrate11());
						pstmt.setInt(++i, qam.getBitrate12());
						pstmt.setInt(++i, qam.getBitrate13());
						pstmt.setInt(++i, qam.getBitrate14());
						pstmt.setInt(++i, qam.getBitrate15());
						pstmt.setInt(++i, qam.getBitrate16());

						pstmt.setInt(++i, qam.getNumOfServices1());
						pstmt.setInt(++i, qam.getNumOfServices2());
						pstmt.setInt(++i, qam.getNumOfServices3());
						pstmt.setInt(++i, qam.getNumOfServices4());
						pstmt.setInt(++i, qam.getNumOfServices5());
						pstmt.setInt(++i, qam.getNumOfServices6());
						pstmt.setInt(++i, qam.getNumOfServices7());
						pstmt.setInt(++i, qam.getNumOfServices8());
						pstmt.setInt(++i, qam.getNumOfServices9());
						pstmt.setInt(++i, qam.getNumOfServices10());
						pstmt.setInt(++i, qam.getNumOfServices11());
						pstmt.setInt(++i, qam.getNumOfServices12());
						pstmt.setInt(++i, qam.getNumOfServices13());
						pstmt.setInt(++i, qam.getNumOfServices14());
						pstmt.setInt(++i, qam.getNumOfServices15());
						pstmt.setInt(++i, qam.getNumOfServices16());
						pstmt.setTimestamp(++i, qam.getCreateTime());
						pstmt.setInt(++i, qam.getBlade());
						pstmt.setString(++i, qam.getNodeId());

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

					sql = "INSERT INTO grusnms.node_event(seq_no,event_id,event_object,description,severity,create_time,handled,event_time,t_user,node_id) "
							+ "VALUES(?,?,?,?,?,?,?,?,?,?) ";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, event.getSeqNo());
					pstmt.setString(2, event.getEventId());
					pstmt.setString(3, event.getEventObject());
					pstmt.setString(4, event.getDescription());
					pstmt.setString(5, event.getSeverity());
					pstmt.setTimestamp(6, event.getCreateTime());
					pstmt.setBoolean(7, event.isHandled());
					pstmt.setTimestamp(8, event.getEventTime());
					pstmt.setString(9, event.getUser());
					pstmt.setString(10, event.getNodeId());
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
}
