package com.don;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling  // Uncomment this to enable email service
public class FileRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileRestApiApplication.class, args);
	}
}
