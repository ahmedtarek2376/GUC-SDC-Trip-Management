package com.sdc.callingapp.tripcarmanagement.car;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sdc.callingapp.tripcarmanagement.AlreadyExistException;
import com.sdc.callingapp.tripcarmanagement.NotFoundException;

@Service
public class CarService {

	@Autowired
	private CarRepository carRepository;
	
	public List<Car> findAllCars(){
		return carRepository.findAll();
	}
	
	public Car findCar(String carID) {
		return carRepository.findByCarID(carID);
	}
	
	public List<Car> findAvailableCars(){
		return carRepository.findByAvailableIsTrue();
	}
	
	public Car updateCarLocation(String carID,double longitude, double latitude) {
	
		Car car = carRepository.findByCarID(carID); 
		
		if(car ==null) {
        	throw new NotFoundException("There is no car with carID = " + carID);
		}
		
		car.setLatitude(latitude);
		car.setLongitude(longitude);
		
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
		
		if (findCar(car.getcarID()) == null) { //if no car with this carID already exists
            carRepository.save(car);
        }else {
        	throw new AlreadyExistException("There is already a car with carID = " + car.getcarID());
        }
		
		return findCar(car.getcarID());
	}

	public void deleteCar(String carID) {
		
		Car car = findCar(carID);
		
		if ( car != null) { //if a car with this carID exists
            carRepository.delete(findCar(carID));
        }else {
        	throw new NotFoundException("There is no car with carID = " + carID);
        }
		
	}

	public Car changeCarFCM(Car car) {
		Car editCar = carRepository.findByCarID(car.getcarID());
		editCar.setCarFcmToken(car.getCarFcmToken());
		return carRepository.save(editCar);
	}

	public Car changeTabletFCM(Car car) {
		Car editCar = carRepository.findByCarID(car.getcarID());
		editCar.setTabletFcmToken(car.getTabletFcmToken());
		return carRepository.save(editCar);
	}
	
}

