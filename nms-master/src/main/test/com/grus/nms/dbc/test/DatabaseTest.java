package com.grus.nms.dbc.test;

import com.grus.nms.dbc.DatabaseConnection;

public class DatabaseTest {

	public static void main(String args[]){
		System.out.println(new DatabaseConnection().getConnection());
	}
}
