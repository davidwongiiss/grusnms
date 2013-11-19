package com.grus.nms.deamon.monitor.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.grus.nms.deamon.monitor.dao.IQamDAO;
import com.grus.nms.deamon.monitor.pojo.QamValue;

public class QamDAOImpl implements IQamDAO {

	private Connection conn = null;
	
	public QamDAOImpl(Connection conn){
		this.conn = conn;
	}
	public boolean doCreate(QamValue qam) throws Exception {
		String sql = "INSERT INTO grusbiz.qam_day_values(node_id,qam1,qam2,qam3,qam4,qam5,qam6,qam7,qam8,qam9,qam10,qam11,qam12,qam13,qam14,qam15,qam16," +
				     " bitrate1,bitrate2,bitrate3,bitrate4,bitrate5,bitrate6,bitrate7,bitrate8,bitrate9,bitrate10,bitrate11,bitrate12,bitrate13,bitrate14,bitrate15,bitrate16," +
				     " numofservices1,numofservices2,numofservices3,numofservices4,numofservices5,numofservices6,numofservices7,numofservices8,numofservices9,numofservices10,numofservices11," +
				     " numofservices12,numofservices13,numofservices14,numofservices15,numofservices16,create_time,blade) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, qam.getNodeId());
		pstmt.setInt(2, qam.getQam1());
		pstmt.setInt(3, qam.getQam2());
		pstmt.setInt(4, qam.getQam3());
		pstmt.setInt(5, qam.getQam4());
		pstmt.setInt(6, qam.getQam5());
		pstmt.setInt(7, qam.getQam6());
		pstmt.setInt(8, qam.getQam7());
		pstmt.setInt(9, qam.getQam8());
		pstmt.setInt(10, qam.getQam9());
		pstmt.setInt(11, qam.getQam10());
		pstmt.setInt(12, qam.getQam11());
		pstmt.setInt(13, qam.getQam12());
		pstmt.setInt(14, qam.getQam13());
		pstmt.setInt(15, qam.getQam14());
		pstmt.setInt(16, qam.getQam15());
		pstmt.setInt(17, qam.getQam16());
		
		pstmt.setInt(18, qam.getBitrate1());
		pstmt.setInt(19, qam.getBitrate2());
		pstmt.setInt(20, qam.getBitrate3());
		pstmt.setInt(21, qam.getBitrate4());
		pstmt.setInt(22, qam.getBitrate5());
		pstmt.setInt(23, qam.getBitrate6());
		pstmt.setInt(24, qam.getBitrate7());
		pstmt.setInt(25, qam.getBitrate8());
		pstmt.setInt(26, qam.getBitrate9());
		pstmt.setInt(27, qam.getBitrate10());
		pstmt.setInt(28, qam.getBitrate11());
		pstmt.setInt(29, qam.getBitrate12());
		pstmt.setInt(30, qam.getBitrate13());
		pstmt.setInt(31, qam.getBitrate14());
		pstmt.setInt(32, qam.getBitrate15());
		pstmt.setInt(33, qam.getBitrate16());
		
		pstmt.setInt(34, qam.getNumOfServices1());
		pstmt.setInt(35, qam.getNumOfServices2());
		pstmt.setInt(36, qam.getNumOfServices3());
		pstmt.setInt(37, qam.getNumOfServices4());
		pstmt.setInt(38, qam.getNumOfServices5());
		pstmt.setInt(39, qam.getNumOfServices6());
		pstmt.setInt(40, qam.getNumOfServices7());
		pstmt.setInt(41, qam.getNumOfServices8());
		pstmt.setInt(42, qam.getNumOfServices9());
		pstmt.setInt(43, qam.getNumOfServices10());
		pstmt.setInt(44, qam.getNumOfServices11());
		pstmt.setInt(45, qam.getNumOfServices12());
		pstmt.setInt(46, qam.getNumOfServices13());
		pstmt.setInt(47, qam.getNumOfServices14());
		pstmt.setInt(48, qam.getNumOfServices15());
		pstmt.setInt(49, qam.getNumOfServices16());
		pstmt.setTimestamp(50, qam.getCreateTime());
		pstmt.setInt(51, qam.getBlade());
		if(pstmt.executeUpdate() > 0){
			return true;
		}
		return false;
	}

}
