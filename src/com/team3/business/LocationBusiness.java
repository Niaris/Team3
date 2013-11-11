package com.team3.business;

import java.util.ArrayList;
import java.util.List;

import com.team3.dataaccess.MySQLConnection;
import com.team3.entities.LocationVO;

public class LocationBusiness {
	
	private MySQLConnection DBConnection;

	public LocationBusiness(MySQLConnection mySQLConnection) {
		this.DBConnection = mySQLConnection;
	}
	
	
	public int registerLocation(LocationVO location) {
	// TODO create the method and return its id
		
		DBConnection.registerLocation(location);
		return 0;
	}
	
	public List<LocationVO> retrieveLocationsByUserPosition (double latitude, double longitude) {
		// TODO create the method
		
		DBConnection.retrieveLocationsByUserPosition(latitude, longitude);
		LocationVO loc1 = new LocationVO("Park Village", 50.870041, -0.090369, "Park Village Name");
		LocationVO loc2 = new LocationVO("Library", 50.865017, -0.089661, "Library Name");
		LocationVO loc3 = new LocationVO("Sussex House", 50.864382, -0.086877, "Sussex House Name");
		LocationVO loc4 = new LocationVO("Amex Stadium", 50.861531, -0.083562, "Amex Stadium Name");
		LocationVO loc5 = new LocationVO("Stanmer Park", 50.866298, -0.093539, "Stanmer Park Name");
		LocationVO loc6 = new LocationVO("Chichester", 50.865892, -0.087145, "Chichester Name");
		LocationVO loc7 = new LocationVO("Falmer Station", 50.862012, -0.087317, "Falmer Station Name");
		List<LocationVO> locList = new ArrayList<LocationVO>();
		locList.add(loc1);
		locList.add(loc2);
		locList.add(loc3);
		locList.add(loc4);
		locList.add(loc5);
		locList.add(loc6);
		locList.add(loc7);
		return locList;
	}
	
}
