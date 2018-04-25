package com.sdc.callingapp.tripcarmanagement.trip;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sdc.callingapp.tripcarmanagement.guc.GucPoints;

@Component
public class TripSeeder implements CommandLineRunner{
	
	@Autowired
	private TripRepository tripRepository;
	
	@Override
	public void run(String... args) throws Exception {
		Trip trip1 = new Trip("car1","user1", GucPoints.B3_U_AREA.getLatLng(), Arrays.asList(GucPoints.C3_U_AREA.getLatLng(), GucPoints.C6_U_AREA.getLatLng()) );
		Trip trip2 = new Trip("car2","user2",GucPoints.B3_U_AREA.getLatLng(), Arrays.asList(GucPoints.C3_U_AREA.getLatLng()) );
		Trip trip3 = new Trip("car3","user3",GucPoints.GATE_1.getLatLng(), Arrays.asList(GucPoints.C6_U_AREA.getLatLng()) );
		Trip trip4 = new Trip("car4","user4",GucPoints.GUC_GYM.getLatLng(), Arrays.asList(GucPoints.GATE_3.getLatLng()) );
		Trip trip5 = new Trip("car5","user5",GucPoints.D4_U_AREA.getLatLng(), Arrays.asList(GucPoints.GUC_GYM.getLatLng(), GucPoints.GATE_4.getLatLng()) );

		this.tripRepository.deleteAll();
		List<Trip> trips = Arrays.asList(trip1, trip2, trip3, trip4, trip5);
		this.tripRepository.saveAll(trips);
	}
}
