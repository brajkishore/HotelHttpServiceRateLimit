package com.hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.hotel.controllers","com.hotel.models"})
public class HotelHttpServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelHttpServiceApplication.class, args);
	}
}
