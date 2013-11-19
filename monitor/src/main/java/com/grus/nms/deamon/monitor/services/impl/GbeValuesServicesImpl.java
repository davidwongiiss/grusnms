package com.grus.nms.deamon.monitor.services.impl;

import java.sql.Timestamp;
import java.util.Date;

import com.grus.nms.deamon.monitor.dbc.DatabaseConnection;
import com.grus.nms.deamon.monitor.factory.DAOFactory;
import com.grus.nms.deamon.monitor.pojo.GbeValue;
import com.grus.nms.deamon.monitor.services.IGbeValuesServices;

public class GbeValuesServicesImpl implements IGbeValuesServices {
	//private DatabaseConnection dbc = new DatabaseConnection();
	private DatabaseConnection dbc = null;
	
	public GbeValuesServicesImpl(DatabaseConnection conn) {
		this.dbc = conn;		
	}

	public boolean insert(GbeValue gbe) throws Exception {
		boolean flag = false;
		try {
			gbe.setCreateTime(new Timestamp(new Date().getTime()));
			flag = DAOFactory.getIGbeValuesDAOInstance(this.dbc.getConnection())
					.doCreateForGbeDayValues(gbe);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			//this.dbc.close();
		}
		return flag;
	}
}