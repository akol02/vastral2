package com.sunbeam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.sunbeam.entities")
public class VastralApplication {

	public static void main(String[] args) {
		SpringApplication.run(VastralApplication.class, args);
	}

}
