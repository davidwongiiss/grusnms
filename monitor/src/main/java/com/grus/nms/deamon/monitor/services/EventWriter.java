package com.grus.nms.deamon.monitor.services;

import com.grus.nms.deamon.monitor.dbc.DatabaseConnection;
import com.grus.nms.deamon.monitor.pojo.EventValue;

public class EventWriter {
	private DatabaseConnection conn = null;
	
	public EventWriter(DatabaseConnection conn) {
		this.conn = conn;
	}
	
	public void handle(EventValue ev) {
		if (conn == null) {
			return;
		}
	}
}
