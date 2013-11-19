package com.grus.nms.factory;

import java.sql.Connection;

import com.grus.nms.dao.IGbeValuesDAO;
import com.grus.nms.dao.INodesDAO;
import com.grus.nms.dao.IQamDAO;
import com.grus.nms.dao.impl.GbeValuesDAOImpl;
import com.grus.nms.dao.impl.NodeDAOImpl;
import com.grus.nms.dao.impl.QamDAOImpl;

public class DAOFactory {

	public static  INodesDAO getINodesDAOInstance(Connection conn){
		return new NodeDAOImpl(conn);
	}
	
	public static IGbeValuesDAO getIGbeValuesDAOInstance(Connection conn){
		return new GbeValuesDAOImpl(conn);
	}
	
	public static IQamDAO getIQamValuesDAOInstance(Connection conn){
		return new QamDAOImpl(conn);
	}
}
