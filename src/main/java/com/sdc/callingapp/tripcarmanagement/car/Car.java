package com.sdc.callingapp.tripcarmanagement.car;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sdc.callingapp.tripcarmanagement.trip.Trip;

@Document(collection = "cars")
public class Car {
	
	@Id
    private String id;
	
	private String carID;
	
	private double longitude;
	private double latitude;
    
	private boolean available;
	
	private Trip currentTrip;
	
    public Car() {
	}
    
    public Car(String carID, double longitude, double latitude, boolean available) {
    	this.carID = carID;
    	this.latitude = latitude;
    	this.longitude = longitude;
    	this.available = available;
    }
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getcarID() {
		return carID;
	}
	public void setcarID(String carID) {
		this.carID = carID;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public Trip getCurrentTrip() {
		return currentTrip;
	}

	public void setCurrentTrip(Trip currentTrip) {
		this.currentTrip = currentTrip;
	}
	
	
	
}
