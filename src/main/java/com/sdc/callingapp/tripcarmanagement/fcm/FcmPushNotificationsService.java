package com.sdc.callingapp.tripcarmanagement.fcm;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FcmPushNotificationsService {
	
	private static final String FIREBASE_SERVER_KEY = "AAAA61cUdk8:APA91bHrrFU6ZaeJfQPW4RNIpqESR4p8nyFAn4nXNyDB9KqVUMNboZhyrV6m0lpnPb_cNF1qEKG6gRUqXHeKrgxvH6PR0ojlLZvVBjgq4sD7n8WLocUeD__Pi_JxsVUxhadib6PXiz2y";
	private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";
	
	
	public FirebaseResponse sendNotification(Push push) {

		HttpEntity<Push> request = new HttpEntity<>(push);

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
	public CompletableFuture<FirebaseResponse> send(HttpEntity<Push> request) {

		RestTemplate restTemplate = new RestTemplate();

		/**
		https://fcm.googleapis.com/fcm/send
		Content-Type:application/json
		Authorization:key=FIREBASE_SERVER_KEY*/

		ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		
		System.out.println(FIREBASE_SERVER_KEY);
		interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
		interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
		restTemplate.setInterceptors(interceptors);

		FirebaseResponse firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, request, FirebaseResponse.class);

		return CompletableFuture.completedFuture(firebaseResponse);
	}
}