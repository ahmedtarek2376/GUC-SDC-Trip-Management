package com.sdc.callingapp.tripcarmanagement.car;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sdc.callingapp.tripcarmanagement.LatLng;

@RestController
@RequestMapping("/car")
public class CarController {
	
	@Autowired
	private CarService carService;
	
	//admin use it to create a new car
	@RequestMapping(method=RequestMethod.POST, value="/admin/create")
	public Car createCar(@RequestBody Car car) {
		
		return carService.createCar(car);
		
	}
	
	//admin use it to change tablet's FCM token
	@RequestMapping(method=RequestMethod.POST, value="/admin/token/tablet")
	public Car tabletToken(@RequestBody Car car) {
		
		return carService.changeTabletFCM(car);
		
	}
		
	
	//admin use it to delete a car by its ID
	@RequestMapping(method=RequestMethod.POST, value="/admin/delete/{carID}")
	public void deleteCar(@PathVariable String carID) {
		
		carService.deleteCar(carID);
		
	}
	
	//use it to retrieve a car by its ID
	@RequestMapping(method=RequestMethod.GET, value="/find/{carID}")
	public Car getCar(@PathVariable String carID) {
		
		return carService.findCar(carID);
		
	}
	
	//use it to retrieve all cars
	@RequestMapping(method=RequestMethod.GET, value="/find/all")
	public List<Car> getAllCars() {
		
		return carService.findAllCars();
		
	}
	
	//use it to retrieve all available cars
	@RequestMapping(method=RequestMethod.GET, value="/find/all/available")
	public List<Car> getAvailableCars() {
		
		return carService.findAvailableCars();
		
	}
	
	//use it to update car's current location
	@RequestMapping(method=RequestMethod.POST, value="/update/location/{carID}")
	public Car updateLocation(@PathVariable String carID, @RequestBody LatLng latlng) {
		
		return carService.updateCarLocation(carID, latlng);
		
	}
	
	//use it when assigning and ending a trip
	@RequestMapping(method=RequestMethod.POST, value="/update/availability/{carID}")
	public Car updateAvailability(@PathVariable String carID, @RequestBody boolean available) {
		
		return carService.updateCarAvailability(carID, available);
		
	}
	//use it to start car's current trip from mobile
	@RequestMapping(method=RequestMethod.GET, value="/trip/start/mobile/{gmail}")
	public Car mobileStartCurrentTrip(@PathVariable String gmail) {
		
		return carService.mobileStart(gmail, 1);
		
	}
	
	//use it to end car's current trip from mobile
	@RequestMapping(method=RequestMethod.GET, value="/trip/end/mobile/{carID}")
	public Car mobileEndCurrentTrip(@PathVariable String carID) {
	
		return carService.endCurrentTrip(carID,1);
		
	}
	
	//use it to cancel car's current trip from mobile
	@RequestMapping(method=RequestMethod.GET, value="/trip/cancel/mobile/{carID}")
	public Car mobileCancelCurrentTrip(@PathVariable String carID) {
		
		return carService.cancelCurrentTrip(carID,1);
		
	}
	
	//use it to start car's current trip from tablet
	@RequestMapping(method=RequestMethod.GET, value="/trip/start/tablet/{carID}")
	public Car tabletStartCurrentTrip(@PathVariable String carID) {
		
		return carService.startCurrentTrip(carID,2);
		
	}
	
	//use it to end car's current trip from tablet
	@RequestMapping(method=RequestMethod.GET, value="/trip/end/tablet/{carID}")
	public Car tabletEndCurrentTrip(@PathVariable String carID) {
	
		return carService.endCurrentTrip(carID,2);
		
	}
	
	//use it to cancel car's current trip from tablet
	@RequestMapping(method=RequestMethod.GET, value="/trip/cancel/tablet/{carID}")
	public Car tabletCancelCurrentTrip(@PathVariable String carID) {
		
		return carService.cancelCurrentTrip(carID,2);
		
	}
}
