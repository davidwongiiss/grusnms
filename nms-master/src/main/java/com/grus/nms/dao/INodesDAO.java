package com.grus.nms.dao;

import java.util.List;

import com.grus.nms.pojo.Nodes;

public interface INodesDAO {

	public List<Nodes> findAllNodes() throws Exception;
}
