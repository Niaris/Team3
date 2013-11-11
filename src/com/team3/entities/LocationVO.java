package com.team3.entities;

import java.io.Serializable;

public class LocationVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private int ID;
	private double Latitude;
	private double Longitude;
	private String Address;
	private String Name;
	
	public LocationVO(String address, double latitude, double longitude, String name) {
		this.Address = address;
		this.Latitude = latitude;
		this.Longitude = longitude;
		this.Name = name;
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
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	
	
}
