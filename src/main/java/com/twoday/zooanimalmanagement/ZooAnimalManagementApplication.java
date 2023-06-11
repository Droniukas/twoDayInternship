package com.twoday.zooanimalmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ZooAnimalManagementApplication {
	public static final String BASE_URL = "/api";

	public static void main(String[] args) {
		SpringApplication.run(ZooAnimalManagementApplication.class, args);
	}

}
