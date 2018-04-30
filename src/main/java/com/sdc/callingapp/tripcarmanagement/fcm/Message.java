package com.sdc.callingapp.tripcarmanagement.fcm;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {

	private String to;

	private String priority;
	
	private HashMap<String,String> data;

	private Notification notification;

	@JsonProperty(value = "registration_ids")
	private List<String> registrationIds;

	
	public Message(List<String> registrationds, String priority, Notification notification, HashMap<String,String> data) {
		this.priority = priority;
		this.notification = notification;
		this.registrationIds = registrationds;
		this.data = data;
	}
	
	public Message(List<String> registrationds, String priority, HashMap<String,String> data) {
		this.priority = priority;
		this.data = data; 
		this.registrationIds = registrationds;
	}
	
	public Message(String to, String priority, Notification notification, HashMap<String,String> data) {
//		data = new JSONObject();
//		try {
//			data.put("Key-1", "JSA Data 1");
//			data.put("Key-2", "JSA Data 2");
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		this.to = to;
		this.priority = priority;
		this.notification = notification;
		this.data = data; 
	}
	
	public Message(String to, String priority, HashMap<String,String> data) {
		this.to = to;
		this.priority = priority;
		this.data = data; 
	}

	
	public Message() {

	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	public List<String> getRegistrationIds() {
		return registrationIds;
	}

	public void setRegistrationIds(List<String> registrationIds) {
		this.registrationIds = registrationIds;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public HashMap<String,String> getData() {
		return data;
	}

	public void setData(HashMap<String,String> data) {
		this.data = data;
	}

}