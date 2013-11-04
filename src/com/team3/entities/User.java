package com.team3.entities;

public class User {

	private int ID;
	private String LastName;
	private String FirstName;
	private String EmailAddress;
	
	public User(String lastName, String firstName, String emailAddress) {
		this.LastName = lastName;
		this.FirstName = firstName;
		this.EmailAddress = emailAddress;
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getEmailAddress() {
		return EmailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		EmailAddress = emailAddress;
	}

}
