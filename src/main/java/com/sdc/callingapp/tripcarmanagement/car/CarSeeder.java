package com.sdc.callingapp.tripcarmanagement.car;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sdc.callingapp.tripcarmanagement.LatLng;

@Component
public class CarSeeder implements CommandLineRunner{
	
	@Autowired
	private CarRepository carRepository;
	
	@Override
	public void run(String... args) throws Exception {
		Car car1 = new Car("car1", "", new LatLng(), false);
		Car car2 = new Car("hadwa", "", new LatLng(29.986225, 31.440492), true);
		Car car3 = new Car("car3", "", new LatLng(), false);
		Car car4 = new Car("abdelrahman", "", new LatLng(29.984971, 31.440341), true);
		Car car5 = new Car("car5", "", new LatLng(), false);
		Car car6 = new Car("tarek", "", new LatLng(29.987298, 31.438829), true);
		Car car7 = new Car("car7", "", new LatLng(), false);
		Car car8 = new Car("car8", "", new LatLng(29.987270, 31.441484), true);
		
		this.carRepository.deleteAll();
		List<Car> cars = Arrays.asList(car1,car2,car3,car4,car5,car6,car7,car8);
		this.carRepository.saveAll(cars);
	}
}
