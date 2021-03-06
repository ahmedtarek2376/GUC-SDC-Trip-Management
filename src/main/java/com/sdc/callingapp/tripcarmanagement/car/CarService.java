package com.sdc.callingapp.tripcarmanagement.car;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sdc.callingapp.tripcarmanagement.NoAvailableCarException;
import com.sdc.callingapp.tripcarmanagement.LatLng;
import com.sdc.callingapp.tripcarmanagement.NotFoundException;
import com.sdc.callingapp.tripcarmanagement.fcm.FcmPushNotificationsService;
import com.sdc.callingapp.tripcarmanagement.fcm.Message;
import com.sdc.callingapp.tripcarmanagement.fcm.Notification;
import com.sdc.callingapp.tripcarmanagement.trip.Trip;
import com.sdc.callingapp.tripcarmanagement.trip.TripDestination;
import com.sdc.callingapp.tripcarmanagement.trip.TripEvent;
import com.sdc.callingapp.tripcarmanagement.trip.TripRepository;

@Service
public class CarService {

	@Autowired
	private CarRepository carRepository;
	
	@Autowired
	private TripRepository tripRepository;
	
	@Autowired
	private FcmPushNotificationsService pushNotification;
	
	public List<Car> findAllCars(){
		return carRepository.findAll();
	}
	
	public Car findCar(String carID) {
		return carRepository.findByCarID(carID);
	}
	
	public List<Car> findAvailableCars(){
		return carRepository.findByAvailableIsTrue();
	}
	
	public Car updateCarLocation(String carID, LatLng latLng) {
	
		Car car = carRepository.findByCarID(carID); 
		
		if(car ==null) {
        	throw new NotFoundException("There is no car with carID = " + carID);
		}
		car.setLatLng(latLng);
		
		carRepository.save(car);
		return car;
	}
	
	public Car updateCarAvailability(String carID, boolean available) {
		
		Car car = carRepository.findByCarID(carID);
		
		if(car ==null) {
        	throw new NotFoundException("There is no car with carID = " + carID);
		}
		
		car.setAvailable(available);
		carRepository.save(car);
		return car;
	}

	public Car createCar(Car car) {
		carRepository.insert(car);
		return findCar(car.getcarID());
	}

	public void deleteCar(String carID) {
		carRepository.delete(findCar(carID));
		
	}

	public Car changeTabletFCM(Car car) {
		Car editCar = carRepository.findByCarID(car.getcarID());
		editCar.setTabletFcmToken(car.getTabletFcmToken());
		return carRepository.save(editCar);
	}

	public Trip startCurrentTrip(String carID, int mode) {
		Car car = carRepository.findByCarID(carID);
		Trip currentTrip = car.getCurrentTrip();
		if(currentTrip!=null) {
			currentTrip.setStartTime(new Date());
			currentTrip.setEvent(TripEvent.START.name());
			car.setCurrentTrip(currentTrip);
			carRepository.save(car);
			updateProfileDb(currentTrip, mode, TripEvent.START.name(), "Your ride has started");
			return tripRepository.save(currentTrip);
		}else {
			throw new NotFoundException("This car has no current trip");
		}

	}
	
	public Trip mobileStart(String gmail, int mode) {
		Car car = carRepository.findByCurrentTripUserID(gmail);
		if(car!=null) {
			Trip currentTrip = car.getCurrentTrip();
			currentTrip.setStartTime(new Date());
			currentTrip.setEvent(TripEvent.START.name());
			car.setCurrentTrip(currentTrip);
			carRepository.save(car);
			updateProfileDb(currentTrip, mode, TripEvent.START.name(), "Your ride has started");
			return tripRepository.save(currentTrip);

		}else {
			throw new NotFoundException("Can not find a car on trip with this userID");
		}
		
	}
	
	public Trip cancelCurrentTrip(String carID, int mode) {
		Car car = carRepository.findByCarID(carID);
		Trip currentTrip = car.getCurrentTrip();
		if(currentTrip!=null) {
			//set End time in current trip
			currentTrip.setCancelTime(new Date());
			currentTrip.setEvent(TripEvent.CANCEL.name());
			updateProfileDb(currentTrip, mode, TripEvent.CANCEL.name(), "Your ride has been canceled");
			//remove current trip from car
			car.setCurrentTrip(null);
			//set the car to available
			car.setAvailable(true);
			carRepository.save(car);
			return tripRepository.save(currentTrip);
		}else {
			throw new NotFoundException("This car has no current trip");
		}
	}
	
	public Trip mobileCancel(String gmail, int mode) {
		Car car = carRepository.findByCurrentTripUserID(gmail);
		if(car!=null) {
			//set Cancel time in current trip
			Trip currentTrip = car.getCurrentTrip();
			currentTrip.setCancelTime(new Date());
			currentTrip.setEvent(TripEvent.CANCEL.name());
			updateProfileDb(currentTrip, mode, TripEvent.CANCEL.name(), "Your ride has been canceled");
			//remove current trip from car
			car.setCurrentTrip(null);
			//set the car to available
			car.setAvailable(true);
			carRepository.save(car);
			return tripRepository.save(currentTrip);
		} else {
			throw new NotFoundException("Can not find a car on trip with this userID");
		}
	}
	
	public Trip endCurrentTrip(String carID, int mode) {
		Car car = carRepository.findByCarID(carID);
		Trip currentTrip = car.getCurrentTrip();
		if(currentTrip!=null) {
			//set End time in current trip
			currentTrip.setEndTime(new Date());
			currentTrip.setEvent(TripEvent.END.name());
			updateProfileDb(currentTrip, mode, TripEvent.END.name(), "Your ride ended. Thank you for using GUC SDC :)");
			//remove current trip from car
			car.setCurrentTrip(null);
			//set the car to available
			car.setAvailable(true);
			carRepository.save(car);
			return tripRepository.save(currentTrip);
		}else {
			throw new NotFoundException("This car has no current trip");
		}
	}
	
	public Trip mobileEnd(String gmail, int mode) {
		Car car = carRepository.findByCurrentTripUserID(gmail);
		if(car!=null) {
			//set Cancel time in current trip
			Trip currentTrip = car.getCurrentTrip();
			currentTrip.setEndTime(new Date());
			currentTrip.setEvent(TripEvent.END.name());
			updateProfileDb(currentTrip, mode, TripEvent.END.name(), "Your ride has ended. Thank you for using GUC SDC :)");
			//remove current trip from car
			car.setCurrentTrip(null);
			//set the car to available
			car.setAvailable(true);
			carRepository.save(car);
			return tripRepository.save(currentTrip);
		} else {
			throw new NotFoundException("Can not find a car on trip with this userID");
		}
	}

	private void updateProfileDb(Trip trip, int mode, String event, String message) {
		
		if(mode==1) { //from mobile
			RestTemplate restTemplate = new RestTemplate();
	        //Trip profileTrip = restTemplate.getForObject("http://localhost:8081/trip/start/" + tripID, Trip.class);
	        System.out.println("Returned Trip = " + trip.toString());
	        notifyTablet(trip, event, message);
		} else if(mode==2){ //from tablet
			//check if the trip has a profile
			if(trip.getUserID()!=null) {
				RestTemplate restTemplate = new RestTemplate();
		        //Trip profileTrip = restTemplate.getForObject("http://localhost:8081/trip/start/" + tripID, Trip.class);
		        System.out.println("Returned Trip = " + trip.toString());
		        notifyPhone(trip, event, message);
			}
		} else if(mode==3) {
			notifyTablet(trip, event, message);
			if(trip.getUserFcmToken() != null) {
				notifyPhone(trip, event, message);
			}
		}
		
	}

	private void notifyPhone(Trip trip, String event, String message2) {
		String mobileFcm = trip.getUserFcmToken();
		
		HashMap<String,String> data = new HashMap<>();
		data.put("EVENT", event);
		data.put("TRIP_ID", trip.getId());
		data.put("CAR_ID", trip.getCarID());
		Notification notification = new Notification("GUC Self-Driving Car", message2);
		Message message = new Message();
		message.setNotification(notification);
		message.setData(data);
		message.setTo(trip.getUserFcmToken());
		message.setPriority("high");
		pushNotification.sendNotification(message);
		
	}

	private void notifyTablet(Trip trip, String event, String message2) {
		String tabletFcm = trip.getTabletFcmToken();
		
		HashMap<String,String> data = new HashMap<>();
		data.put("EVENT", event);
		data.put("TRIP_ID", trip.getId());
		data.put("CAR_ID", trip.getCarID());
		Notification notification = new Notification("GUC Self-Driving Car", message2);
		Message message = new Message();
		message.setNotification(notification);
		message.setData(data);
		message.setTo(tabletFcm);
		message.setPriority("high");
		pushNotification.sendNotification(message);
	}

	public Car carArrivePickup(String carID) {
		Car car = carRepository.findByCarID(carID);
		if(car.getCurrentTrip() != null) {
			Trip currentTrip = car.getCurrentTrip();
			currentTrip.setCarArriveTime(new Date());
			currentTrip.setEvent(TripEvent.ARRIVE_PICKUP.name());
			car.setCurrentTrip(currentTrip);
			tripRepository.save(currentTrip);
			updateProfileDb(currentTrip, 3, TripEvent.ARRIVE_PICKUP.name(), "Your car has arrived to the pickup location");
			return carRepository.save(car);
		}else {
			throw new NotFoundException("Car has no current trip");
		}
	}

	public Car carArriveFinal(String carID) {
		Car car = carRepository.findByCarID(carID);
		if(car.getCurrentTrip() != null) {
			Trip currentTrip = car.getCurrentTrip();
			currentTrip.setCarArriveFinal(new Date());
			currentTrip.setEvent(TripEvent.ARRIVE_FINAL.name());
			currentTrip.getDestinations().get(currentTrip.getDestinations().size()-1).setArrived(true);
			car.setCurrentTrip(currentTrip);
			tripRepository.save(currentTrip);
			updateProfileDb(currentTrip, 3, TripEvent.ARRIVE_FINAL.name(), "You reached your last destination");
			return carRepository.save(car);
		}else {
			throw new NotFoundException("Car has no current trip");
			
		}
	}

	public Car carArriveDestination(String carID) {
		Car car = carRepository.findByCarID(carID);
		if(car.getCurrentTrip() != null) {
			Trip currentTrip = car.getCurrentTrip();
			/////////////////////
			List<TripDestination> destinations = currentTrip.getDestinations();
			for(int i=0 ; i<destinations.size() ; i++) {
				TripDestination d = destinations.get(i);
				if(! d.isArrived()) {
					d.setArrived(true);
					destinations.set(i, d);
					break;
				}
			}
			currentTrip.setDestinations(destinations);
			currentTrip.setEvent(TripEvent.ARRIVE_DESTINATION.name());
			/////////////////////
			car.setCurrentTrip(currentTrip);
			tripRepository.save(currentTrip);
			updateProfileDb(currentTrip, 3, TripEvent.ARRIVE_DESTINATION.name(), "Destination arrived");
			return carRepository.save(car);
		}else {
			throw new NotFoundException("Car has no current trip");
		}
	}

	public Trip tabletContinue(String carID, int mode) {
		Car car = carRepository.findByCarID(carID);
		Trip currentTrip = car.getCurrentTrip();
		if(currentTrip!=null) {
			currentTrip.setEvent(TripEvent.CONTINUE.name());
			car.setCurrentTrip(currentTrip);
			carRepository.save(car);
			updateProfileDb(currentTrip, mode, TripEvent.CONTINUE.name(), "Car moving to next destination");
			return tripRepository.save(currentTrip);
		}else {
			throw new NotFoundException("This car has no current trip");
		}
	}

	public Trip mobileContinue(String gmail, int mode) {
		Car car = carRepository.findByCurrentTripUserID(gmail);
		Trip currentTrip = car.getCurrentTrip();
		if(currentTrip!=null) {
			currentTrip.setEvent(TripEvent.CONTINUE.name());
			car.setCurrentTrip(currentTrip);
			carRepository.save(car);
			updateProfileDb(currentTrip, mode, TripEvent.CONTINUE.name(), "Car moving to next destination");
			return tripRepository.save(currentTrip);
		}else {
			throw new NotFoundException("This car has no current trip");
		}
	}

	public Map<String, Object> checkUserOnTrip(String gmail) {
		Car car = carRepository.findByCurrentTripUserID(gmail);
//		if(car == null) {
//			return Collections.singletonMap("free", true);
//		}else {
//			return Collections.singletonMap("free", false);
//		}
		Map<String,Object> map = new HashMap<String, Object>();
		if(car==null) {
			 map.put("FREE", true);
			 map.put("TRIP_ID", null);
		}else {
			 map.put("FREE", false);
			 map.put("TRIP_ID", car.getCurrentTrip().getId());
		}
		
		return map;
	}

	public Trip mobileChange(String gmail, Trip trip, int mode) {
		Car car = carRepository.findByCurrentTripUserID(gmail);
		Trip currentTrip = car.getCurrentTrip();
		if(currentTrip!=null) {
			currentTrip.setEvent(TripEvent.CHANGE_DESTINATION.name());
			currentTrip.setDestinations(trip.getDestinations());
			car.setCurrentTrip(currentTrip);
			carRepository.save(car);
			updateProfileDb(currentTrip, mode, TripEvent.CHANGE_DESTINATION.name(), "Your ride's destination(s) have been successfuly modified");
			return tripRepository.save(currentTrip);
		}else {
			throw new NotFoundException("This car has no current trip");
		}	
		
	}

	public Trip tabletChange(String carID, Trip trip, int mode) {
		Car car = carRepository.findByCarID(carID);
		Trip currentTrip = car.getCurrentTrip();
		if(currentTrip!=null) {
			currentTrip.setEvent(TripEvent.CHANGE_DESTINATION.name());
			currentTrip.setDestinations(trip.getDestinations());
			car.setCurrentTrip(currentTrip);
			carRepository.save(car);
			updateProfileDb(currentTrip, mode, TripEvent.CHANGE_DESTINATION.name(), "Your ride's destination(s) have been successfuly modified");
			return tripRepository.save(currentTrip);
		}else {
			throw new NotFoundException("This car has no current trip");
		}
	}

	public Trip createTrip(Trip trip) {
		trip.setRequestTime(new Date());
		Car car = carRepository.findByCarID(trip.getCarID());
		trip.setTabletFcmToken(car.getTabletFcmToken());
		car.setAvailable(false);
		tripRepository.insert(trip);
		car.setCurrentTrip(trip);
		carRepository.save(car); 
		return trip;
	}
	
	
	
}

