package com.volvo.congestiontaxcalculator.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.volvo.congestiontaxcalculator.vehicle.Vehicle;

import jakarta.annotation.PostConstruct;
//New Initializer class to return the vehicle  type bases on input values
@Component("vehicleTypeInitializer")
public class VehicleTypeInitializer {

	@Autowired
	private List<Vehicle> vehicles;

	private static final Map<String, Vehicle> cache = new HashMap<String, Vehicle>();

	@PostConstruct
	public void init() {
		for (Vehicle vehicle : vehicles) {
			cache.put(vehicle.getVehicleType().toLowerCase(), vehicle);
		}
	}

	public Vehicle getVehicleType(String vehicleType) {
		Vehicle vehicle = cache.get(vehicleType.toLowerCase());
		return vehicle;
	}
}
