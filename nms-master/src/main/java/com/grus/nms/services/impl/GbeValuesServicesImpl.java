package com.grus.nms.services.impl;

import java.sql.Timestamp;
import java.util.Date;

import com.grus.nms.dbc.DatabaseConnection;
import com.grus.nms.factory.DAOFactory;
import com.grus.nms.pojo.GbeValues;
import com.grus.nms.services.IGbeValuesServices;

public class GbeValuesServicesImpl implements IGbeValuesServices {

	private DatabaseConnection dbc = new DatabaseConnection();
	@Override
	public boolean insertGbeValuesForGbeDay(GbeValues gbe) throws Exception {
		boolean flag = false;
		try{
			gbe.setCreateTime(new Timestamp(new Date().getTime()));
			flag = DAOFactory.getIGbeValuesDAOInstance(this.dbc.getConnection()).doCreateForGbeDayValues(gbe);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			this.dbc.close();
		}
		return flag;
	}

}
