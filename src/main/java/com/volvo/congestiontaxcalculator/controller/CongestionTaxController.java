package com.volvo.congestiontaxcalculator.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.volvo.congestiontaxcalculator.model.CongestionTaxResponse;
import com.volvo.congestiontaxcalculator.service.CongestionTaxCalculator;
import com.volvo.congestiontaxcalculator.util.VehicleTypeInitializer;
import com.volvo.congestiontaxcalculator.vehicle.Vehicle;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//New Controller class is added to support receiving of API request

@RestController
public class CongestionTaxController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CongestionTaxController.class);

	@Autowired
	private VehicleTypeInitializer vehicleTypeInitializer;

	@Autowired
	private CongestionTaxCalculator congestionTaxCalculator;

	// Method to get tax details for a given date and a given vehicle type
	@RequestMapping(value = "/congestionTax/getTaxRate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public CongestionTaxResponse getTaxRate(HttpServletRequest request, HttpServletResponse response,
			@RequestHeader(name = "date", required = true) String date,
			@RequestHeader(name = "vehicleType", required = true) String vehicleType) {

		LOGGER.info("getting tax details for date {} and vehicle type {}", date, vehicleType);
		CongestionTaxResponse taxResponse = null;
		try {
			Vehicle vehicle = vehicleTypeInitializer.getVehicleType(vehicleType);

			taxResponse = congestionTaxCalculator.getTaxDetails(vehicle, date);
		} catch (ParseException e) {
			LOGGER.error("Exception occured during processing {}", e.getMessage());
		}

		return taxResponse;
	}

	// Method to get tax details for a list of dates and list of vehicle types
	@RequestMapping(value = "/congestionTax/getAllTaxRates", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CongestionTaxResponse> getAllTaxRates(HttpServletRequest request, HttpServletResponse response,
			@RequestHeader(name = "date", required = true) String date,
			@RequestHeader(name = "vehicleType", required = true) String vehicleType) {

		LOGGER.info("getting tax details for date {} and vehicle type {}", date, vehicleType);
		List<CongestionTaxResponse> taxResponseList = null;
		try {
			List<String> dateList = Arrays.asList(date.split(","));
			List<String> vehicleTypeList = Arrays.asList(vehicleType.split(","));
			List<Vehicle> vehicles = new ArrayList<Vehicle>();

			for (String s : vehicleTypeList) {
				Vehicle vehicle = vehicleTypeInitializer.getVehicleType(s);
				vehicles.add(vehicle);
			}

			taxResponseList = congestionTaxCalculator.getAllTaxDetails(vehicles, dateList);
		} catch (ParseException e) {
			LOGGER.error("Exception occured during processing {}", e.getMessage());
		}

		return taxResponseList;
	}

}
