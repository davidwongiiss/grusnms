package com.grus.nms.deamon.monitor.factory;

import com.grus.nms.deamon.monitor.dbc.DatabaseConnection;
import com.grus.nms.deamon.monitor.services.IGbeValuesServices;
import com.grus.nms.deamon.monitor.services.INodeServices;
import com.grus.nms.deamon.monitor.services.IQamValuesServices;
import com.grus.nms.deamon.monitor.services.impl.GbeValuesServicesImpl;
import com.grus.nms.deamon.monitor.services.impl.NodeServicesImpl;
import com.grus.nms.deamon.monitor.services.impl.QamValuesServicesImpl;

public class ServicesFactory {
	public static INodeServices getINodeServicesInstance(DatabaseConnection conn){
		return new NodeServicesImpl(conn);
	}
	
	public static IGbeValuesServices getIGbeValuesServicesInstance(DatabaseConnection conn){
		return new GbeValuesServicesImpl(conn);
	}
	public static IQamValuesServices getIQamValuesServicesInstance(DatabaseConnection conn){
		return new QamValuesServicesImpl(conn);
	}
}