package com.grus.nms.deamon.monitor.services.impl;

import java.sql.Timestamp;
import java.util.Date;

import com.grus.nms.deamon.monitor.dbc.DatabaseConnection;
import com.grus.nms.deamon.monitor.factory.DAOFactory;
import com.grus.nms.deamon.monitor.pojo.QamValue;
import com.grus.nms.deamon.monitor.services.IQamValuesServices;

public class QamValuesServicesImpl implements IQamValuesServices {
	//private DatabaseConnection dbc = new DatabaseConnection();
	private DatabaseConnection dbc = null;
	
	public QamValuesServicesImpl(DatabaseConnection conn) {
		this.dbc = conn;		
	}	

	public boolean insert(QamValue qam) throws Exception {
		boolean flag = false;
		try {
			qam.setCreateTime(new Timestamp(new Date().getTime()));
			flag = DAOFactory.getIQamValuesDAOInstance(this.dbc.getConnection())
					.doCreate(qam);
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
