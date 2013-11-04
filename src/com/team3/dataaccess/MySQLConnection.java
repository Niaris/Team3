package com.team3.dataaccess;

import java.util.List;

import com.team3.entities.Location;
import com.team3.entities.Review;

public class MySQLConnection {

	public MySQLConnection () {
		connectToServer();
	}
	
	private void connectToServer() {
		// TODO
		
	}
	
	
	public void registerLocation(Location location) {
		// TODO Auto-generated method stub
		
	}

	public List<Location> retrieveLocationsByUserPosition(int latitude, int longitude) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addReviewToLocation(Review review, int locationID) {
		// TODO Auto-generated method stub
		
	}

	public List<Review> retrieveReviewsList(int locationID) {
		// TODO Auto-generated method stub
		return null;
	}

	public void open() {
		// TODO Auto-generated method stub
		
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

}
