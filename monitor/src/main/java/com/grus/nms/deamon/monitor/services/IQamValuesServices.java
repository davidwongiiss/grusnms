package com.grus.nms.deamon.monitor.services;

import com.grus.nms.deamon.monitor.pojo.QamValue;

public interface IQamValuesServices {
	public boolean insert(QamValue qam) throws Exception;
}
