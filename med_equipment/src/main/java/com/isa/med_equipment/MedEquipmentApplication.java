package com.isa.med_equipment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.isa.med_equipment.beans")
@EnableJpaRepositories(basePackages = "com.isa.med_equipment.repository")
public class MedEquipmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedEquipmentApplication.class, args);
		System.out.println("Hello world!");
	}

}
