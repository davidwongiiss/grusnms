package com.grus.nms.deamon.monitor.dao;

import com.grus.nms.deamon.monitor.pojo.QamValue;

public interface IQamDAO {
	public boolean doCreate(QamValue qam) throws Exception;
}
