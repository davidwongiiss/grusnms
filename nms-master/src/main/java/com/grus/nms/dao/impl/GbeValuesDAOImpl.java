package com.grus.nms.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.grus.nms.dao.IGbeValuesDAO;
import com.grus.nms.pojo.GbeValues;

public class GbeValuesDAOImpl implements IGbeValuesDAO {

	private Connection conn = null;

	public GbeValuesDAOImpl(Connection conn) {
		this.conn = conn;
	}

	@Override
	public boolean doCreateForGbeDayValues(GbeValues gbe) throws Exception {
		String sql = " INSERT INTO grusbiz.gbe_day_values(node_id,numofservices1,numofservices2,numofservices3,numofservices4,numofservices5,numofservices6,numofservices7,numofservices8," +
				     " multicastbitrate1,multicastbitrate2,multicastbitrate3,multicastbitrate4,multicastbitrate5,multicastbitrate6,multicastbitrate7,multicastbitrate8,create_time) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, gbe.getNodeId());
		pstmt.setInt(2, gbe.getNumberOfServices1());
		pstmt.setInt(3, gbe.getNumberOfServices2());
		pstmt.setInt(4, gbe.getNumberOfServices3());
		pstmt.setInt(5, gbe.getNumberOfServices4());
		pstmt.setInt(6, gbe.getNumberOfServices5());
		pstmt.setInt(7, gbe.getNumberOfServices6());
		pstmt.setInt(8, gbe.getNumberOfServices7());
		pstmt.setInt(9, gbe.getNumberOfServices8());
		pstmt.setInt(10, gbe.getMulticastBitrate1());
		pstmt.setInt(11, gbe.getMulticastBitrate2());
		pstmt.setInt(12, gbe.getMulticastBitrate3());
		pstmt.setInt(13, gbe.getMulticastBitrate4());
		pstmt.setInt(14, gbe.getMulticastBitrate5());
		pstmt.setInt(15, gbe.getMulticastBitrate6());
		pstmt.setInt(16, gbe.getMulticastBitrate7());
		pstmt.setInt(17, gbe.getMulticastBitrate8());
		pstmt.setTimestamp(18, gbe.getCreateTime());
		if(pstmt.executeUpdate() > 0){
			return true;
		}
		return false;
	}

}
