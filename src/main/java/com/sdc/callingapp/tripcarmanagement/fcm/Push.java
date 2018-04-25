package com.sdc.callingapp.tripcarmanagement.fcm;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Push {
	
	private JSONObject data;
	

	private String to;

	private String priority;

	private Notification notification;

	@JsonProperty(value = "registration_ids")
	private List<String> registrationIds;

	
	public Push(String priority, Notification notification, List<String> registrationds) {
		this.priority = priority;
		this.notification = notification;
		this.registrationIds = registrationds;
	}

	
	public Push(String to, String priority, Notification notification) {
		data = new JSONObject();
		try {
			data.put("Key-1", "JSA Data 1");
			data.put("Key-2", "JSA Data 2");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.to = to;
		this.priority = priority;
		this.notification = notification;
	}

	
	public Push() {

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

}