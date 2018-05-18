package com.sdc.callingapp.tripcarmanagement.trip;

import java.util.HashMap;
import java.util.List;
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
import com.sdc.callingapp.tripcarmanagement.fcm.Message;

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
		HashMap<String,String> data = new HashMap<>();
		data.put("Key-1", "JSA Data 1");
		data.put("Key-2", "JSA Data 2");
		Notification notification = new Notification("My App", "Test");
		Message message = new Message();
		//message.setNotification(notification);
		message.setData(data);
		message.setTo("enPP-ZIzEiw:APA91bGGYoYhYQaFft7IFHx2cfI-wm21BSQucIr-q9iqdOy_ssUAK-oJk63hOXm3dXM5TGZ9x0jsAhxnakzX-2Vmm-mPhc4-xSZYEFYyYwzXyPJJ1GGX3vY8aXt_rIJYxYZH6re4KyHC");
		message.setPriority("high");
		//Message message = new Message("enPP-ZIzEiw:APA91bGGYoYhYQaFft7IFHx2cfI-wm21BSQucIr-q9iqdOy_ssUAK-oJk63hOXm3dXM5TGZ9x0jsAhxnakzX-2Vmm-mPhc4-xSZYEFYyYwzXyPJJ1GGX3vY8aXt_rIJYxYZH6re4KyHC","high", notification);
		pushNotification.sendNotification(message);
		
		return new ResponseEntity<Trip>(HttpStatus.OK);
	}


}
