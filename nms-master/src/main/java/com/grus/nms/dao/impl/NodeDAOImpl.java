package com.grus.nms.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.grus.nms.dao.INodesDAO;
import com.grus.nms.pojo.Nodes;

public class NodeDAOImpl implements INodesDAO {

	private Connection conn = null;
	public NodeDAOImpl(Connection conn){
		this.conn = conn;
	}
	@Override
	public List<Nodes> findAllNodes() throws Exception {
		List<Nodes> list = new ArrayList<Nodes>();
		String sql = " SELECT  id, name,model,description,mac,ip, ipv6,create_time, update_time,creator,updater FROM grusbiz.nodes  ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		Nodes node = null;
		while(rs.next()){
			node = new Nodes();
			node.setId(rs.getString(1));
			node.setName(rs.getString(2));
			node.setModel(rs.getString(3));
			node.setDescriptioin(rs.getString(4));
			node.setMac(rs.getString(5));
			node.setIp(rs.getString(6));
			node.setIpv6(rs.getString(7));
			node.setCreateTime(rs.getDate(8));
			node.setUpdateTime(rs.getDate(9));
			node.setCreator(rs.getString(10));
			node.setUpdater(rs.getString(11));
			list.add(node);
		}
		return list;
	}

}
