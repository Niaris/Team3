package com.team3.business;

import java.util.List;

import com.team3.dataaccess.MySQLConnection;
import com.team3.entities.Location;

public class LocationBusiness {
	
	private MySQLConnection DBConnection;

	public LocationBusiness(MySQLConnection mySQLConnection) {
		this.DBConnection = mySQLConnection;
	}
	
	
	public void registerLocation(Location location) {
	// TODO create the method
		
		DBConnection.registerLocation(location);
	}
	
	public List<Location> retrieveLocationsByUserPosition (int latitude, int longitude) {
		// TODO create the method
		
		DBConnection.retrieveLocationsByUserPosition(latitude, longitude);
		return null;
	}
	
}
