package com.sdc.callingapp.tripcarmanagement.guc;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/gucpoints")
public class GucPointsController {
	
	//use it to retrieve a trip by its ID
			@RequestMapping(method=RequestMethod.GET, value="/all")
			public List<GucPlace> getAllTrips() {

				return GucPoints.getAllGucPoints();

			}

}
