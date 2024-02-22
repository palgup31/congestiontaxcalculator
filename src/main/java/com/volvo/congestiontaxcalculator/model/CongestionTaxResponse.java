package com.volvo.congestiontaxcalculator.model;
//New model class to construct and return the response
public class CongestionTaxResponse {

	private String date;
	
	private String vehicleType;
	
	private int tax;
	
	private String additionalInfo;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public int getTax() {
		return tax;
	}

	public void setTax(int tax) {
		this.tax = tax;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	@Override
	public String toString() {
		return "CongestionTaxResponse [date=" + date + ", vehicleType=" + vehicleType + ", tax=" + tax
				+ ", additionalInfo=" + additionalInfo + "]";
	}
	
}
