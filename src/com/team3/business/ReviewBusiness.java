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
	
	public void addReviewToLocation(ReviewVO review, LocationVO location) throws Exception {
		if(location.getID() == 0) {
			int newID = LocationBUS.registerLocation(location);
			review.setLocationID(newID);
		}
		validateReviewVO(review);
		DBConnection.addReview(review);
	}
	
	private void validateReviewVO(ReviewVO review) throws Exception {
		if(review.getComment().isEmpty() || review.getRating() < 1) {
			throw new Exception("Rating and Comment are mandatory fields");
		}
		if(review.getUserID() <= 0) {
			throw new Exception("User is not logged in");
		}
		if(review.getLocationID() <= 0) {
			throw new Exception("Location not found. Try again.");
		}
	}

	public List<ReviewVO> retrieveReviewsList (int locationID) {
		// TODO create the method
		DBConnection.retrieveReviewsList(locationID);
		return null;
	}
	
}
