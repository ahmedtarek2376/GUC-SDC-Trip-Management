package com.sdc.callingapp.tripcarmanagement.trip;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc.callingapp.tripcarmanagement.NotFoundException;
import com.sdc.callingapp.tripcarmanagement.car.Car;
import com.sdc.callingapp.tripcarmanagement.car.CarRepository;

@Service
public class TripService {

	@Autowired
	private TripRepository tripRepository;
	@Autowired
	private CarRepository carRepository;

	public Trip createTrip(Trip trip) {
		trip.setRequestTime(new Date());
		return assingToAvailableCar(trip);
	}

	private Trip assingToAvailableCar(Trip trip) {
		List<Car> availableCars = carRepository.findByAvailableIsTrue();
		if(availableCars.size()>0){
			Car car = availableCars.get(0);
			trip.setCarID(car.getcarID());
			trip.setTabletFcmToken(car.getTabletFcmToken());
			trip.setEvent(TripEvent.CAR_ON_WAY.name());
			car.setAvailable(false);
			tripRepository.insert(trip);
			car.setCurrentTrip(trip);
			carRepository.save(car); 
		}
		return trip;
	}

	public Trip findTrip(String tripID) {

		return tripRepository.findById(tripID).orElse(null);

	}

	public Trip startTrip(String tripID) {
		Trip trip = tripRepository.findById(tripID).orElse(null);
		
		if(trip == null) {
			throw new NotFoundException("There is no trip with Id = " + tripID);
		}else {
			//can't start a started or canceled trip
			if(trip.getStartTime() == null && trip.getCancelTime() == null) {
				trip.setStartTime(new Date());
				tripRepository.save(trip);
			}
		}
		return trip;
	}

	public Trip endTrip(String tripID) {
		Trip trip = tripRepository.findById(tripID).orElse(null);
		
		if(trip == null) {
			throw new NotFoundException("There is no trip with Id = " + tripID);
		}else {
			//can't end a trip that did not start or canceled
			if(trip.getStartTime() != null && trip.getCancelTime() == null) {
				trip.setEndTime(new Date());
				tripRepository.save(trip);	
				freeTheCar(trip);
			}
		}
		return trip;
	}

	public Trip cancelTrip(String tripID) {
		Trip trip = tripRepository.findById(tripID).orElse(null);
		
		if(trip == null) {
			throw new NotFoundException("There is no trip with Id = " + tripID);
		}else {
			//can't cancel an ended trip
			if(trip.getEndTime() == null) {
				trip.setCancelTime(new Date());
				tripRepository.save(trip);	
				freeTheCar(trip);
			}
		}
		return trip;
	}
	
	public void freeTheCar(Trip trip) {
		String carID = trip.getCarID();
		Car car = carRepository.findByCarID(carID);
		car.setAvailable(true);
		car.setCurrentTrip(null);
		
		carRepository.save(car);
	}

	public List<Trip> findAllTrips() {
		
		return tripRepository.findAll();
	}


}