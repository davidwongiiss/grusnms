package com.grus.nms.deamon.monitor.dao;

import java.util.List;

import com.grus.nms.deamon.monitor.pojo.Node;

public interface INodesDAO {
	public List<Node> findAllNodes() throws Exception;
}
