package com.sdc.callingapp.tripcarmanagement.car;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CarSeeder implements CommandLineRunner{
	
	@Autowired
	private CarRepository carRepository;
	
	@Override
	public void run(String... args) throws Exception {
		Car car1 = new Car("car1", 11.2, 11.9, false);
		Car car2 = new Car("car2", 12.2, 12.9, true);
		Car car3 = new Car("car3", 13.2, 13.9, false);
		Car car4 = new Car("car4", 14.2, 14.9, true);
		Car car5 = new Car("car5", 15.2, 15.9, false);
		Car car6 = new Car("car6", 16.2, 16.9, true);
		Car car7 = new Car("car7", 17.2, 17.9, false);
		Car car8 = new Car("car8", 18.2, 18.9, true);
		
		this.carRepository.deleteAll();
		List<Car> cars = Arrays.asList(car1,car2,car3,car4,car5,car6,car7,car8);
		this.carRepository.saveAll(cars);
	}
}
