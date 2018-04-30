package com.sdc.callingapp.tripcarmanagement.trip;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sdc.callingapp.tripcarmanagement.fcm.FcmPushNotificationsService;
import com.sdc.callingapp.tripcarmanagement.fcm.Notification;
import com.sdc.callingapp.tripcarmanagement.fcm.Push;

@RestController
@RequestMapping("/trip")
public class TripController {

	@Autowired
	private TripService tripService;
	
	@Autowired
	private FcmPushNotificationsService pushNotification;

	//use it to create a new trip
	@RequestMapping(method=RequestMethod.POST, value="/create")
	public Trip createTrip(@RequestBody Trip trip) {

		return tripService.createTrip(trip);

	}
	
	//use it to retrieve a trip by its ID
		@RequestMapping(method=RequestMethod.GET, value="/find/all")
		public List<Trip> getAllTrips() {

			return tripService.findAllTrips();

		}
	
	//use it to retrieve a trip by its ID
	@RequestMapping(method=RequestMethod.GET, value="/find/{tripID}")
	public Trip getTrip(@PathVariable String tripID) {

		return tripService.findTrip(tripID);

	}

	//use it to start a trip
	@RequestMapping(method=RequestMethod.POST, value="/start/{tripID}")
	public Trip startTrip(@PathVariable String tripID) {

		return tripService.startTrip(tripID);

	}

	//use it to end a trip
	@RequestMapping(method=RequestMethod.POST, value="/end/{tripID}")
	public Trip endTrip(@PathVariable String tripID) {

		return tripService.endTrip(tripID);
	}

	//use it to cancel a trip
	@RequestMapping(method=RequestMethod.POST, value="/cancel/{tripID}")
	public Trip cancelTrip(@PathVariable String tripID) {

		return tripService.cancelTrip(tripID);
	}
	
	//use it to test fcm
	@RequestMapping(value = "/push", method = RequestMethod.GET)
	public ResponseEntity<?> push() {

		Notification notification = new Notification("My App", "Test");
		Push push = new Push("eKYzG_k0UFA:APA91bFgr6A87evf8jMQRXEgb6Xhjg6CmnB93Rb1or061bOwH2_Z6rhIWlfazA3yYXaqK0i18RROXTJdOWkxVnzM7N1okqxDYXhlQMNJ7-y1Aym6qY_2reX3nATmoBTgs2t2MRUP5ZRa","high", notification);
		pushNotification.sendNotification(push);
		
		return new ResponseEntity<Trip>(HttpStatus.OK);
	}


}
