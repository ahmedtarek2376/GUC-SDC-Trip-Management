package com.sdc.callingapp.tripcarmanagement.fcm;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FcmPushNotificationsService {
	
	@Value("${my.fcm.key}")
	private String FIREBASE_SERVER_KEY;
	private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";
	
	
	public FirebaseResponse sendNotification(Message message) {

		HttpEntity<Message> request = new HttpEntity<>(message);

		CompletableFuture<FirebaseResponse> pushNotification = this.send(request);
		CompletableFuture.allOf(pushNotification).join();

		FirebaseResponse firebaseResponse = null;

		try {
			firebaseResponse = pushNotification.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return firebaseResponse;
	}
	
	@Async
	public CompletableFuture<FirebaseResponse> send(HttpEntity<Message> request) {

		RestTemplate restTemplate = new RestTemplate();

		/**
		https://fcm.googleapis.com/fcm/send
		Content-Type:application/json
		Authorization:key=FIREBASE_SERVER_KEY*/

		ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		
		interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
		interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
		restTemplate.setInterceptors(interceptors);

		FirebaseResponse firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, request, FirebaseResponse.class);

		return CompletableFuture.completedFuture(firebaseResponse);
	}
}