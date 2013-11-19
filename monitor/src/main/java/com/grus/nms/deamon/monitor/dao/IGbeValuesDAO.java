package com.grus.nms.deamon.monitor.dao;

import com.grus.nms.deamon.monitor.pojo.GbeValue;

public interface IGbeValuesDAO {
	public boolean doCreateForGbeDayValues(GbeValue gbe) throws Exception;
}
