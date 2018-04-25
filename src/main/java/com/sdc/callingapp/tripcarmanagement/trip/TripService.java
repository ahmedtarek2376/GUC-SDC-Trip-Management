package com.sdc.callingapp.tripcarmanagement.trip;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc.callingapp.tripcarmanagement.NotFoundException;

@Service
public class TripService {

	@Autowired
	private TripRepository tripRepository;

	public String createTrip(Trip trip) {
		trip.setRequestTime(new Date());
		String tripID="";
		tripRepository.insert(trip);
		tripID= trip.getId();

		return tripID;
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
			}
		}
		return trip;
	}

	public List<Trip> findAllTrips() {
		
		return tripRepository.findAll();
	}


}