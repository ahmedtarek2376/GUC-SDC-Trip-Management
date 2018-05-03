package com.sdc.callingapp.tripcarmanagement.guc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guc")
public class GucController {
	
	@Autowired
	private GucRepository gucRepository;
	
	//get all GUC places
	@RequestMapping(method=RequestMethod.GET, value="/pins")
	public List<GucPlace> getAllGucPlaces() {

		return gucRepository.findAll();

	}
		

}
