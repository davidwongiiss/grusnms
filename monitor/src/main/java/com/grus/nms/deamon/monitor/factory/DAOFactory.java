package com.grus.nms.deamon.monitor.factory;

import java.sql.Connection;

import com.grus.nms.deamon.monitor.dao.IGbeValuesDAO;
import com.grus.nms.deamon.monitor.dao.INodesDAO;
import com.grus.nms.deamon.monitor.dao.IQamDAO;
import com.grus.nms.deamon.monitor.dao.impl.GbeValuesDAOImpl;
import com.grus.nms.deamon.monitor.dao.impl.NodeDAOImpl;
import com.grus.nms.deamon.monitor.dao.impl.QamDAOImpl;

public class DAOFactory {
	public static INodesDAO getINodesDAOInstance(Connection conn) {
		return new NodeDAOImpl(conn);
	}

	public static IGbeValuesDAO getIGbeValuesDAOInstance(Connection conn) {
		return new GbeValuesDAOImpl(conn);
	}

	public static IQamDAO getIQamValuesDAOInstance(Connection conn) {
		return new QamDAOImpl(conn);
	}
}
