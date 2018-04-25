package com.sdc.callingapp.tripcarmanagement.car;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	public Car updateLocation(@PathVariable String carID, @RequestBody double longitude, @RequestBody double latitude) {
		
		return carService.updateCarLocation(carID, longitude, latitude);
		
	}
	
	//use it when assigning and ending a trip
	@RequestMapping(method=RequestMethod.POST, value="/update/availability/{carID}")
	public Car updateAvailability(@PathVariable String carID, @RequestBody boolean available) {
		
		return carService.updateCarAvailability(carID, available);
		
	}
	
}
