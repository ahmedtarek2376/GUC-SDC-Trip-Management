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
	
	private String event;
	
	private Date requestTime;
	private Date carArriveTime;
	private Date startTime;
	private Date endTime;
	private Date cancelTime;
	private Date carArriveFinal;
	
	private LatLng pickupLocation;
	private List<TripDestination> destinations;
	
	private String carID;
	private String tabletFcmToken;
	private String userID;
	private String userFcmToken;
	
	public Trip() {
	}
	
	public Trip(String carID, String userID, LatLng pickupLocation, List<TripDestination> destinations) {
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

	public List<TripDestination> getDestinations() {
		return destinations;
	}

	public void setDestinations(List<TripDestination> destinations) {
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

	public String getUserFcmToken() {
		return userFcmToken;
	}

	public void setUserFcmToken(String userFcmToken) {
		this.userFcmToken = userFcmToken;
	}

	public String getTabletFcmToken() {
		return tabletFcmToken;
	}

	public void setTabletFcmToken(String tabletFcmToken) {
		this.tabletFcmToken = tabletFcmToken;
	}

	public Date getCarArriveTime() {
		return carArriveTime;
	}

	public void setCarArriveTime(Date carArriveTime) {
		this.carArriveTime = carArriveTime;
	}

	public Date getCarArriveFinal() {
		return carArriveFinal;
	}

	public void setCarArriveFinal(Date carArriveFinal) {
		this.carArriveFinal = carArriveFinal;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}	

}
