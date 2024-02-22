package com.volvo.congestiontaxcalculator.vehicle;

import org.springframework.stereotype.Component;
//updated the class with component annotation to initialize vehicle object dynamically based on the input values
@Component("motorbike")
public class Motorbike implements Vehicle {
	
	@Override
	public String getVehicleType() {
		return "Motorbike";
	}
}
