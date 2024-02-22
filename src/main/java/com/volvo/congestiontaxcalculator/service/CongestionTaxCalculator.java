package com.volvo.congestiontaxcalculator.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.volvo.congestiontaxcalculator.model.CongestionTaxResponse;
import com.volvo.congestiontaxcalculator.vehicle.Vehicle;

@Service("congestionTaxCalculator")
public class CongestionTaxCalculator {

	private static final Map<String, Integer> tollFreeVehicles = new HashMap<>();
	private final Map<Vehicle, Integer> maxFeeInIntervalMap; //add new Hash map for calculating single charge rule
	static {
		tollFreeVehicles.put("Motorbike", 0); //updated the value as motorbike
		tollFreeVehicles.put("Bus", 1);
		tollFreeVehicles.put("EmergencyVehicle", 2);
		tollFreeVehicles.put("DiplomatVehicle", 3);
		tollFreeVehicles.put("ForeignVehicle", 4);
		tollFreeVehicles.put("MilitaryVehicle", 5);
	}

	
	public CongestionTaxCalculator() {
		maxFeeInIntervalMap = new HashMap<>(); 
	}
    
	// New method to calculate tax details and return the response for given vehicle type and dates
	public CongestionTaxResponse getTaxDetails(Vehicle vehicle, String dateString) throws ParseException {

		CongestionTaxResponse response = new CongestionTaxResponse();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = dateFormat.parse(dateString);
		Date[] dates = { date };

		int tax = getTax(vehicle, dates);

		String addInfo = isSigleChargeRuleApplicable(tax, vehicle) ? "Single Charge Rule Applied" : "Tax Applied";

		response.setDate(dateString);
		response.setVehicleType(vehicle.getVehicleType());
		response.setTax(tax);
		response.setAdditionalInfo(addInfo);

		return response;
	}

	//// New method to calculate tax details and return the response for list of vehicle type and list of dates
	public List<CongestionTaxResponse> getAllTaxDetails(List<Vehicle> vehicles, List<String> dates)
			throws ParseException {

		List<CongestionTaxResponse> responseList = new ArrayList<CongestionTaxResponse>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (String d : dates) {
			for (Vehicle v : vehicles) {
				Date date = dateFormat.parse(d);
				Date[] dateArray = { date };

				int tax = getTax(v, dateArray);

				String addInfo = isSigleChargeRuleApplicable(tax, v) ? "Single Charge Rule Applied" : "Tax Applied";
				CongestionTaxResponse response = new CongestionTaxResponse();
				response.setDate(d);
				response.setVehicleType(v.getVehicleType());
				response.setTax(tax);
				response.setAdditionalInfo(addInfo);

				responseList.add(response);
			}

		}

		return responseList;
	}

	public int getTax(Vehicle vehicle, Date[] dates) {
		int totalFee = 0;
		Date previousDate = null; // added start date or previous date variable as null to calculate max time for single charge rule
		maxFeeInIntervalMap.put(vehicle, 0);
        //logic for calculating single charge rule rates
		for (Date date : dates) {
			if (previousDate != null && isWithinInterval(previousDate, date)) {
				int fee = getTollFee(date, vehicle);
				if (fee > maxFeeInIntervalMap.get(vehicle)) {
					maxFeeInIntervalMap.put(vehicle, fee);
				}
			} else {
				totalFee += maxFeeInIntervalMap.get(vehicle);
				maxFeeInIntervalMap.put(vehicle, getTollFee(date, vehicle));
			}
			previousDate = date;
		}
		totalFee += maxFeeInIntervalMap.get(vehicle);

		return Math.min(totalFee, 60); // Cap the total fee at 60 SEK
	}

	public int getMaxFeeInInterval(Vehicle vehicle) {
		return maxFeeInIntervalMap.get(vehicle);
	}

	private boolean isTollFreeVehicle(Vehicle vehicle) {
		if (vehicle == null)
			return false;
		String vehicleType = vehicle.getVehicleType();
		return tollFreeVehicles.containsKey(vehicleType);
	}
    
	public int getTollFee(Date date, Vehicle vehicle) {
		if (isTollFreeDate(date) || isTollFreeVehicle(vehicle))
			return 0;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
       //updated the condition for time as it was calculating wrong output earlier
		
		if ((hour == 6 && minute >= 0 && minute <= 29) || (hour == 6 && minute >= 30 && minute <= 59))
			return 8;
		else if ((hour == 7 || (hour == 8 && minute <= 29)) || (hour == 17 && minute >= 0 && minute <= 59)
				|| (hour == 15 && minute <= 29))
			return 13;
		else if ((hour >= 8 || hour <= 14 && minute <= 59) || (hour == 18 && minute >= 0 && minute <= 29))
			return 8;
		else if ((hour == 15 && minute >= 30) || (hour == 16))
			return 18;
		else
			return 0;
	}
//Calender library use for date logics instead of Date Library
	private boolean isTollFreeDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int month = calendar.get(Calendar.MONTH);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

		return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY
				|| (calendar.get(Calendar.YEAR) == 2013 && ((month == Calendar.JANUARY && dayOfMonth == 1)
						|| (month == Calendar.MARCH && (dayOfMonth == 28 || dayOfMonth == 29))
						|| (month == Calendar.APRIL && (dayOfMonth == 1 || dayOfMonth == 30))
						|| (month == Calendar.MAY && (dayOfMonth == 1 || dayOfMonth == 8 || dayOfMonth == 9))
						|| (month == Calendar.JUNE && (dayOfMonth == 5 || dayOfMonth == 6 || dayOfMonth == 21))
						|| (month == Calendar.JULY) || (month == Calendar.NOVEMBER && dayOfMonth == 1)
						|| (month == Calendar.DECEMBER
								&& (dayOfMonth == 24 || dayOfMonth == 25 || dayOfMonth == 26 || dayOfMonth == 31))));
	}

	private boolean isWithinInterval(Date previousDate, Date currentDate) {
		long diffInMillis = currentDate.getTime() - previousDate.getTime();
		long minutes = diffInMillis / (1000 * 60);
		return minutes <= 60;
	}

	private boolean isSigleChargeRuleApplicable(int tax, Vehicle vehicleType) {
		boolean flag = false;
		if (tax == getMaxFeeInInterval(vehicleType)) {
			flag = true;
		}
		return flag;
	}
}