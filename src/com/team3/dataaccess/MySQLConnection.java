package com.team3.dataaccess;

import java.util.List;

import com.team3.entities.LocationVO;
import com.team3.entities.ReviewVO;

public class MySQLConnection {

	public MySQLConnection () {
		connectToServer();
	}
	
	private void connectToServer() {
		// TODO
		
	}
	
	
	public void registerLocation(LocationVO location) {
		// TODO Auto-generated method stub
		
	}

	public List<LocationVO> retrieveLocationsByUserPosition(int latitude, int longitude) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addReview(ReviewVO review) {
		// TODO Auto-generated method stub
		
	}

	public List<ReviewVO> retrieveReviewsList(int locationID) {
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
