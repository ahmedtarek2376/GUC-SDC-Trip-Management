package com.sdc.callingapp.tripcarmanagement.guc;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.sdc.callingapp.tripcarmanagement.LatLng;

@Component
public class PointsSeeder implements CommandLineRunner{
	
	@Autowired
	private GucRepository gucRepository;
	
	@Override
	public void run(String... args) throws Exception {
		GucPlace C3_U_AREA = new GucPlace("C3",new LatLng(29.986905, 31.439592));
	    GucPlace C6_U_AREA = new GucPlace("C6",new LatLng(29.986169, 31.438699));
	    GucPlace D4_U_AREA = new GucPlace("D4",new LatLng(29.986926, 31.440630));
	    GucPlace B3_U_AREA = new GucPlace("B3",new LatLng(29.985092, 31.439574));
	    GucPlace GUC_GYM = new GucPlace("Gym",new LatLng(29.985884, 31.438944));
	    GucPlace GATE_1 = new GucPlace("Gate 1",new LatLng(29.984504, 31.440095));
	    GucPlace GATE_3 = new GucPlace("Gate 3",new LatLng(29.987276, 31.438320));
	    GucPlace GATE_4 = new GucPlace("Gate 4",new LatLng(29.988201, 31.438331));
		List<GucPlace> places = Arrays.asList(C3_U_AREA,C6_U_AREA,D4_U_AREA,B3_U_AREA,GUC_GYM,GATE_1,GATE_3,GATE_4);
		this.gucRepository.deleteAll();
		this.gucRepository.saveAll(places);
	}

}
