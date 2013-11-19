package com.grus.nms.services.impl;

import java.sql.Timestamp;
import java.util.Date;

import com.grus.nms.dbc.DatabaseConnection;
import com.grus.nms.factory.DAOFactory;
import com.grus.nms.pojo.QamValues;
import com.grus.nms.services.IQamValuesServices;

public class QamValuesServicesImpl implements IQamValuesServices {

	private DatabaseConnection dbc = new DatabaseConnection();
	
	@Override
	public boolean insertQamValuesForDay(QamValues qam) throws Exception {
		boolean flag = false;
		try{
			qam.setCreateTime(new Timestamp(new Date().getTime()));
			flag = DAOFactory.getIQamValuesDAOInstance(this.dbc.getConnection()).doCreate(qam);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			this.dbc.close();
		}
		return flag;
	}

}
