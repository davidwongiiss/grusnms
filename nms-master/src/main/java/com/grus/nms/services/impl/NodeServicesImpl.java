package com.grus.nms.services.impl;

import java.util.List;

import com.grus.nms.dbc.DatabaseConnection;
import com.grus.nms.factory.DAOFactory;
import com.grus.nms.pojo.Nodes;
import com.grus.nms.services.INodeServices;

public class NodeServicesImpl implements INodeServices {

	private DatabaseConnection dbc = new DatabaseConnection();
	
	
	@Override
	public List<Nodes> findAll() throws Exception {
		List<Nodes> list = null;
		try{
			list = DAOFactory.getINodesDAOInstance(dbc.getConnection()).findAllNodes();
		}catch(Exception e){
			throw e;
		}finally{
			this.dbc.close();
		}
		return list;
	}

}
