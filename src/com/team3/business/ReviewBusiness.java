package com.team3.business;

import java.util.ArrayList;
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
		if(review.getUser().getID() <= 0) {
			throw new Exception("User is not logged in");
		}
		if(review.getLocationID() <= 0) {
			throw new Exception("Location not found. Try again.");
		}
	}

	public List<ReviewVO> retrieveReviewsList (int locationID) {
		// TODO create the method
		DBConnection.retrieveReviewsList(locationID);
		List<ReviewVO> reviewsList = new ArrayList<ReviewVO>();
		reviewsList.add(new ReviewVO(new UserVO("Brown", "Charlie", "charlieBrown@gmail.com"), locationID, 4, "12-10-2013", "12:13", "It is good", ""));
		reviewsList.add(new ReviewVO(new UserVO("White", "Jake", "charlieBrown@gmail.com"), locationID, 5, "12-09-2013", "12:20", "It is bad", ""));
		reviewsList.add(new ReviewVO(new UserVO("Jerry", "Luke", "charlieBrown@gmail.com"), locationID, 1, "12-10-2013", "12:13", "It is great", ""));
		reviewsList.add(new ReviewVO(new UserVO("Holy", "Jessica", "charlieBrown@gmail.com"), locationID, 3, "12-10-2013", "12:13", "It is awful", ""));
		
		return reviewsList;
	}

	public UserVO getUserByID(int userID) {
		UserVO user = new UserVO("dasd", "dasdasd", "dasdasd");
		user.setID(userID);
		return user;
	}
	
}
