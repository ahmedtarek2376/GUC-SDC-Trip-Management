package com.sdc.callingapp.tripcarmanagement.guc;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface GucRepository extends MongoRepository<GucPlace, String> {
	
}
