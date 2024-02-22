# Congestion Tax Calculator - Pallavi Gupta(PersonnalNUm- 199212312343)

Welcome the Volvo Cars Congestion Tax Calculator assignment.

This repository contains a developer [assignment](ASSIGNMENT.md) used as a basis for candidate intervew and evaluation.

Clone this repository to get started. Due to a number of reasons, not least privacy, you will be asked to zip your solution and mail it in, instead of submitting a pull-request. In order to maintain an unbiased reviewing process, please ensure to **keep your name or other Personal Identifiable Information (PII) from the code**.


Instructions:

Import the project to eclipse File -> Import -> Maven -> Existing Maven Projects into workspace

Open the CongestiontaxcalculatorApplication.java and Run As Java Application

The application starts running with default port 8080

The application provides two end points
1) To get tax details for a specific vechicle type and a specified date
2) To get tax details for a list of vehicles and list of dates
3) In postman application follow the below instructions to test

To Test the application for a specific date and vehicleType:
1) In post man provide URL - http://<host>:<port>/congestionTax/getTaxRate
2) Set the following header value
      date         <date value> 
      vehicleType  <type of vehicle>

Sample header value: 

date          2013-01-14 21:00:00
vehicleType   car

set the the method type to GET and run the request

To Test the application for list of dates and vehicleTypes:
1) In post man provide URL - http://<host>:<port>/congestionTax/getAllTaxRates
2) Set the following header value
      date         <date value1>,<date value2>
      vehicleType  <type of vehicle1>,<type of vehicle2>

Sample header value: 

date          2013-01-14 21:00:00,2013-01-15 21:00:00
vehicleType   car,bus

set the the method type to GET and run the request