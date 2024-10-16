package com.film.blue_rabb;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@EnableMongoRepositories
@SpringBootApplication
public class BlueRabbApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlueRabbApplication.class, args);
	}

}
