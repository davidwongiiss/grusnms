package com.grus.nms.deamon.monitor.services;

import java.util.List;

import com.grus.nms.deamon.monitor.pojo.Node;

public interface INodeServices {
	public List<Node> findAll() throws Exception;
}
