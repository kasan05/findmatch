package com.matrimony.findmatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.resilience.annotation.EnableResilientMethods;

@SpringBootApplication
@EnableResilientMethods
@EnableCaching
public class FindmatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(FindmatchApplication.class, args);
	}

}
