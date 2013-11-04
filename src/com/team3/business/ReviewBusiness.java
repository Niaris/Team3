package com.team3.business;

import java.util.List;

import com.team3.dataaccess.MySQLConnection;
import com.team3.entities.*;

public class ReviewBusiness {
	
	private MySQLConnection DBConnection;

	public ReviewBusiness(MySQLConnection mySQLConnection) {
		this.DBConnection = mySQLConnection;
		
	}
	
	public void addReviewToLocation(Review review, int locationID) {
		// TODO create the method
		DBConnection.addReviewToLocation(review, locationID);
	}
	
	public List<Review> retrieveReviewsList (int locationID) {
		// TODO create the method
		DBConnection.retrieveReviewsList(locationID);
		return null;
	}
	
}
