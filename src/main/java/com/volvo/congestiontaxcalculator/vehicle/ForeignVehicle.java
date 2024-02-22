package com.volvo.congestiontaxcalculator.vehicle;

import org.springframework.stereotype.Component;
//updated the class with component annotation to initialize vehicle object dynamically based on the input values
@Component("foreignVehicle")
public class ForeignVehicle implements Vehicle{
	@Override
	public String getVehicleType() {
        return "ForeignVehicle";
    }
}
