package com.grus.nms.services;

import java.util.List;

import com.grus.nms.pojo.Nodes;

public interface INodeServices {

	
	public List<Nodes> findAll() throws Exception;
}
