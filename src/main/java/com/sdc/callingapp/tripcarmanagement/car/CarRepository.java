package com.sdc.callingapp.tripcarmanagement.car;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends MongoRepository<Car, String> {
	
    public Car findByCarID(String carID);
    public List<Car> findByAvailableIsTrue();
    
}