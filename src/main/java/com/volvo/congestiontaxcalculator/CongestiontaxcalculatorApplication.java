package com.volvo.congestiontaxcalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

// The default main method is created with springboot configuration to support running of application locally with embedded server

@SpringBootApplication
@ComponentScan({"com.volvo.congestiontaxcalculator"})
public class CongestiontaxcalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CongestiontaxcalculatorApplication.class, args);
	}

}
