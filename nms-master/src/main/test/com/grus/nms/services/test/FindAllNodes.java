package com.grus.nms.services.test;

import java.util.List;

import com.grus.nms.factory.ServicesFactory;
import com.grus.nms.pojo.Nodes;

public class FindAllNodes {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		List<Nodes> list = null;
		try {
			list = ServicesFactory.getINodeServicesInstance().findAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(list.size());
	}

}
