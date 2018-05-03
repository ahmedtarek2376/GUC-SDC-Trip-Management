package com.sdc.callingapp.tripcarmanagement.car;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sdc.callingapp.tripcarmanagement.AlreadyExistException;
import com.sdc.callingapp.tripcarmanagement.LatLng;
import com.sdc.callingapp.tripcarmanagement.NotFoundException;
import com.sdc.callingapp.tripcarmanagement.fcm.FcmPushNotificationsService;
import com.sdc.callingapp.tripcarmanagement.fcm.Message;
import com.sdc.callingapp.tripcarmanagement.fcm.Notification;
import com.sdc.callingapp.tripcarmanagement.trip.Trip;
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
		
//		if (findCar(car.getcarID()) == null) { //if no car with this carID already exists
//            carRepository.save(car);
//        }else {
//        	throw new AlreadyExistException("There is already a car with carID = " + car.getcarID());
//        }
		
		carRepository.insert(car);
		return findCar(car.getcarID());
	}

	public void deleteCar(String carID) {
		carRepository.delete(findCar(carID));
//		Car car = findCar(carID);
//		
//		if ( car != null) { //if a car with this carID exists
//            carRepository.delete(findCar(carID));
//        }else {
//        	throw new NotFoundException("There is no car with carID = " + carID);
//        }
		
	}

	public Car changeTabletFCM(Car car) {
		Car editCar = carRepository.findByCarID(car.getcarID());
		editCar.setTabletFcmToken(car.getTabletFcmToken());
		return carRepository.save(editCar);
	}

	public Car startCurrentTrip(String carID, int mode) {
		Car car = carRepository.findByCarID(carID);
		if(car.getCurrentTrip()!=null) {
			Trip currentTrip = car.getCurrentTrip();
			currentTrip.setStartTime(new Date());
			car.setCurrentTrip(currentTrip);
			carRepository.save(car);
			tripRepository.save(currentTrip);
			updateProfileDb(currentTrip, mode, "STARTED");
		}
		return carRepository.save(car);
	}
	
	public Car cancelCurrentTrip(String carID, int mode) {
		Car car = carRepository.findByCarID(carID);
		
		if(car.getCurrentTrip()!=null) {
			//set End time in current trip
			Trip currentTrip = car.getCurrentTrip();
			currentTrip.setCancelTime(new Date());
			//update Trip in Trip DB
			tripRepository.save(currentTrip);
			updateProfileDb(currentTrip, mode, "CANCELED");
		}
		//remove current trip from car
		car.setCurrentTrip(null);
		//set the car to available
		car.setAvailable(true);
		carRepository.save(car);
		return carRepository.save(car);
	}
	
	public Car endCurrentTrip(String carID, int mode) {
		Car car = carRepository.findByCarID(carID);
		//set End time in current trip
		if(car.getCurrentTrip()!=null) {
			//set End time in current trip
			Trip currentTrip = car.getCurrentTrip();
			currentTrip.setEndTime(new Date());
			//update Trip in Trip DB
			tripRepository.save(currentTrip);
			updateProfileDb(currentTrip, mode, "ENDED");
		}
		//remove current trip from car
		car.setCurrentTrip(null);
		//set the car to available
		car.setAvailable(true);
		carRepository.save(car);
		return carRepository.save(car);
	}

	private void updateProfileDb(Trip trip, int mode, String action) {
		
		if(mode==1) { //from mobile
			RestTemplate restTemplate = new RestTemplate();
	        //Trip profileTrip = restTemplate.getForObject("http://localhost:8081/trip/start/" + tripID, Trip.class);
	        System.out.println("Returned Trip = " + trip.toString());
	        notifyTablet(trip, action);
		} else { //from tablet
			//check if the trip has a profile
			if(trip.getUserID()!=null) {
				RestTemplate restTemplate = new RestTemplate();
		        //Trip profileTrip = restTemplate.getForObject("http://localhost:8081/trip/start/" + tripID, Trip.class);
		        System.out.println("Returned Trip = " + trip.toString());
		        notifyPhone(trip, action);
			}
		}
		
	}

	private void notifyPhone(Trip trip, String action) {
		String mobileFcm = trip.getUserFcmToken();
		
		HashMap<String,String> data = new HashMap<>();
		data.put("STATUS", action);
		data.put("TRIP_ID", trip.getId());
		data.put("CAR_ID", trip.getCarID());
		Notification notification = new Notification("GUC Self-Driving Car", "Trip update, " + action);
		Message message = new Message();
		message.setNotification(notification);
		message.setData(data);
		message.setTo(trip.getUserFcmToken());
		message.setPriority("high");
		pushNotification.sendNotification(message);
		
	}

	private void notifyTablet(Trip trip, String action) {
		String tabletFcm = trip.getTabletFcmToken();
		
		HashMap<String,String> data = new HashMap<>();
		data.put("STATUS", action);
		data.put("TRIP_ID", trip.getId());
		data.put("CAR_ID", trip.getCarID());
		Notification notification = new Notification("GUC Self-Driving Car", "Trip update, " + action);
		Message message = new Message();
		message.setNotification(notification);
		message.setData(data);
		message.setTo("enPP-ZIzEiw:APA91bGGYoYhYQaFft7IFHx2cfI-wm21BSQucIr-q9iqdOy_ssUAK-oJk63hOXm3dXM5TGZ9x0jsAhxnakzX-2Vmm-mPhc4-xSZYEFYyYwzXyPJJ1GGX3vY8aXt_rIJYxYZH6re4KyHC");
		message.setPriority("high");
		pushNotification.sendNotification(message);
	}
	
	
	
}

