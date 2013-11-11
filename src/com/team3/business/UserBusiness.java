package com.team3.business;

import android.os.AsyncTask;

import com.team3.dataaccess.MySQLConnection;

public class UserBusiness {

	private MySQLConnection DBConnection;
	
	public UserBusiness(MySQLConnection dbConnection) {
		DBConnection = dbConnection;
	}

	public void loginUser () {
		// TODO
		
	}

	public void RegisterUser(String email, String name) {
		DBConnection.RegisterUser(email, name);
	}
		
}
