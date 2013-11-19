package com.grus.nms.dao;

import com.grus.nms.pojo.QamValues;

public interface IQamDAO {

	public boolean doCreate(QamValues qam) throws Exception;
}
