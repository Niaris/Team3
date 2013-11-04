package com.team3.business;

import java.util.List;

import com.team3.dataaccess.MySQLConnection;
import com.team3.entities.*;

public class ReviewBusiness {
	
	private MySQLConnection DBConnection;
	private LocationBusiness LocationBUS;
	
	public ReviewBusiness(MySQLConnection mySQLConnection) {
		this.DBConnection = mySQLConnection;
		this.LocationBUS = new LocationBusiness(DBConnection);
	}
	
	public void addReviewToLocation(ReviewVO review, LocationVO location) {
		// TODO continue the method
		if(location.getID() == 0) {
			int newID = LocationBUS.registerLocation(location);
			review.setLocationID(newID);
		}
		DBConnection.addReview(review);
	}
	
	public List<ReviewVO> retrieveReviewsList (int locationID) {
		// TODO create the method
		DBConnection.retrieveReviewsList(locationID);
		return null;
	}
	
}
