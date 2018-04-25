package com.sdc.callingapp.tripcarmanagement.trip;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sdc.callingapp.tripcarmanagement.LatLng;

@Document(collection = "trips")
public class Trip {
	@Id
    private String id;
	
	private Date requestTime;
	private Date startTime;
	private Date endTime;
	private Date cancelTime;
	
	private LatLng pickupLocation;
	private List<LatLng> destinations;
	
	private String carID;
	private String carFcmToken;
	private String userID;
	private String userFcmToken;
	
	public Trip() {
	}
	
	public Trip(String carID, String userID, LatLng pickupLocation, List<LatLng> destinations) {
		this.requestTime = new Date();
		this.carID = carID;
		this.userID = userID;
		this.pickupLocation = pickupLocation;
		this.destinations = destinations;
	}

	public String getId() {
		return id;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public LatLng getPickupLocation() {
		return pickupLocation;
	}

	public void setPickupLocation(LatLng pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public List<LatLng> getDestinations() {
		return destinations;
	}

	public void setDestinations(List<LatLng> destinations) {
		this.destinations = destinations;
	}

	public String getCarID() {
		return carID;
	}

	public void setCarID(String carID) {
		this.carID = carID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}
	
	

}
