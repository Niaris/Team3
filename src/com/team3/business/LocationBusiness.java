package com.team3.business;

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
	
	public List<LocationVO> retrieveLocationsByUserPosition (int latitude, int longitude) {
		// TODO create the method
		
		DBConnection.retrieveLocationsByUserPosition(latitude, longitude);
		return null;
	}
	
}
