package com.grus.nms.factory;

import com.grus.nms.services.IGbeValuesServices;
import com.grus.nms.services.INodeServices;
import com.grus.nms.services.IQamValuesServices;
import com.grus.nms.services.impl.GbeValuesServicesImpl;
import com.grus.nms.services.impl.NodeServicesImpl;
import com.grus.nms.services.impl.QamValuesServicesImpl;

public class ServicesFactory {

	public static INodeServices getINodeServicesInstance(){
		return new NodeServicesImpl();
	}
	
	public static IGbeValuesServices getIGbeValuesServicesInstance(){
		return new GbeValuesServicesImpl();
	}
	public static IQamValuesServices getIQamValuesServicesInstance(){
		return new QamValuesServicesImpl();
	}
}
