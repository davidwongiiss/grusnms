package com.grus.nms.services.test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.grus.nms.factory.ServicesFactory;
import com.grus.nms.pojo.Nodes;
import com.grus.nms.xml.QAMXml;

public class InsertDemo {

	public static void main(String args[]) throws Exception{
		ExecutorService threadPool = Executors.newCachedThreadPool();
		List<Nodes> nodes = ServicesFactory.getINodeServicesInstance().findAll();
		for(Nodes node:nodes){
			threadPool.execute(new QAMXml(node));
		}
	}
}
