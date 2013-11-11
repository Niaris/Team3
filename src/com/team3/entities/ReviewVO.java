package com.team3.entities;

public class ReviewVO {

	private int ID;
	private int LocationID;
	private int rating;
	private String Date;
	private String Time;
	private String Comment;
	private String ImagePath;
	private UserVO User;
	
	
	public ReviewVO(UserVO user, int locationID, int rating, String date,
			String time, String comment, String imagePath) {
		this.User = user;
		this.LocationID = locationID;
		this.rating = rating;
		this.Date = date;
		this.Time = time;
		this.Comment = comment;
		this.ImagePath = imagePath;
	}
	
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public UserVO getUser() {
		return User;
	}
	public int getLocationID() {
		return LocationID;
	}
	public void setLocationID(int locationID) {
		LocationID = locationID;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	public String getComment() {
		return Comment;
	}
	public void setComment(String comment) {
		Comment = comment;
	}
	public String getImagePath() {
		return ImagePath;
	}
	public void setImagePath(String imagePath) {
		ImagePath = imagePath;
	}
	
	
}
