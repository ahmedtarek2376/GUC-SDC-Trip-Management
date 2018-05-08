package com.sdc.callingapp.tripcarmanagement.car;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sdc.callingapp.tripcarmanagement.AlreadyExistException;
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

	public Trip startCurrentTrip(String carID, int mode) {
		Car car = carRepository.findByCarID(carID);
		Trip currentTrip = car.getCurrentTrip();
		if(currentTrip!=null) {
			currentTrip.setStartTime(new Date());
			currentTrip.setEvent(TripEvent.START.name());
			car.setCurrentTrip(currentTrip);
			carRepository.save(car);
			updateProfileDb(currentTrip, mode, TripEvent.START.name());
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
			updateProfileDb(currentTrip, mode, TripEvent.START.name());
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
			updateProfileDb(currentTrip, mode, TripEvent.CANCEL.name());
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
			updateProfileDb(currentTrip, mode, TripEvent.CANCEL.name());
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
			updateProfileDb(currentTrip, mode, TripEvent.END.name());
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
			updateProfileDb(currentTrip, mode, TripEvent.END.name());
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

	private void updateProfileDb(Trip trip, int mode, String event) {
		
		if(mode==1) { //from mobile
			RestTemplate restTemplate = new RestTemplate();
	        //Trip profileTrip = restTemplate.getForObject("http://localhost:8081/trip/start/" + tripID, Trip.class);
	        System.out.println("Returned Trip = " + trip.toString());
	        notifyTablet(trip, event);
		} else if(mode==2){ //from tablet
			//check if the trip has a profile
			if(trip.getUserID()!=null) {
				RestTemplate restTemplate = new RestTemplate();
		        //Trip profileTrip = restTemplate.getForObject("http://localhost:8081/trip/start/" + tripID, Trip.class);
		        System.out.println("Returned Trip = " + trip.toString());
		        notifyPhone(trip, event);
			}
		} else if(mode==3) {
			notifyTablet(trip, event);
			if(trip.getUserFcmToken() != null) {
				notifyPhone(trip, event);
			}
		}
		
	}

	private void notifyPhone(Trip trip, String event) {
		String mobileFcm = trip.getUserFcmToken();
		
		HashMap<String,String> data = new HashMap<>();
		data.put("EVENT", event);
		data.put("TRIP_ID", trip.getId());
		data.put("CAR_ID", trip.getCarID());
		Notification notification = new Notification("GUC Self-Driving Car", "Trip update, " + event);
		Message message = new Message();
		message.setNotification(notification);
		message.setData(data);
		message.setTo(trip.getUserFcmToken());
		message.setPriority("high");
		pushNotification.sendNotification(message);
		
	}

	private void notifyTablet(Trip trip, String event) {
		String tabletFcm = trip.getTabletFcmToken();
		
		HashMap<String,String> data = new HashMap<>();
		data.put("EVENT", event);
		data.put("TRIP_ID", trip.getId());
		data.put("CAR_ID", trip.getCarID());
		Notification notification = new Notification("GUC Self-Driving Car", "Trip update, " + event);
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
			updateProfileDb(currentTrip, 3, TripEvent.ARRIVE_PICKUP.name());
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
			car.setCurrentTrip(currentTrip);
			tripRepository.save(currentTrip);
			updateProfileDb(currentTrip, 3, TripEvent.ARRIVE_FINAL.name());
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
			updateProfileDb(currentTrip, 3, TripEvent.ARRIVE_DESTINATION.name());
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
			updateProfileDb(currentTrip, mode, TripEvent.CONTINUE.name());
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
			updateProfileDb(currentTrip, mode, TripEvent.CONTINUE.name());
			return tripRepository.save(currentTrip);
		}else {
			throw new NotFoundException("This car has no current trip");
		}
	}

	public Trip checkUserOnTrip(String gmail) {
		Car car = carRepository.findByCurrentTripUserID(gmail);
		if(car!=null) {
			return car.getCurrentTrip();
		}else {
			return null;
		}
	}
	
	
	
}

