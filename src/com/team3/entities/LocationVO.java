package com.team3.entities;

import java.io.Serializable;

public class LocationVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private int ID;
	private String Address;
	private double Latitude;
	private double Longitude;
	
	public LocationVO(String address, double latitude, double longitude) {
		this.Address = address;
		this.Latitude = latitude;
		this.Longitude = longitude;
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public double getLatitude() {
		return Latitude;
	}
	public void setLatitude(double latitude) {
		Latitude = latitude;
	}
	public double getLongitude() {
		return Longitude;
	}
	public void setLongitude(double longitude) {
		Longitude = longitude;
	}
	
	
	
}
