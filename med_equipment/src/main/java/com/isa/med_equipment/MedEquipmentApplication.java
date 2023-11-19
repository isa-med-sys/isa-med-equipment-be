package com.isa.med_equipment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan
@EnableJpaRepositories
public class MedEquipmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedEquipmentApplication.class, args);
		System.out.println("Hello world!");
	}
}