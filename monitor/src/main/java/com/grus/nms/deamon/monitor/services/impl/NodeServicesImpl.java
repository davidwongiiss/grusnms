package com.grus.nms.deamon.monitor.services.impl;

import java.util.List;

import com.grus.nms.deamon.monitor.dbc.DatabaseConnection;
import com.grus.nms.deamon.monitor.factory.DAOFactory;
import com.grus.nms.deamon.monitor.pojo.Node;
import com.grus.nms.deamon.monitor.services.INodeServices;

public class NodeServicesImpl implements INodeServices {
	//private DatabaseConnection dbc = new DatabaseConnection();
	private DatabaseConnection dbc = null;
	
	public NodeServicesImpl(DatabaseConnection conn) {
		this.dbc = conn;		
	}
	
	public List<Node> findAll() throws Exception {
		List<Node> list = null;
		try {
			list = DAOFactory.getINodesDAOInstance(dbc.getConnection())
					.findAllNodes();
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			//this.dbc.close();
		}
		return list;
	}

}
