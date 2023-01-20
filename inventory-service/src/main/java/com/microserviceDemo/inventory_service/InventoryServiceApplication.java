package com.microserviceDemo.inventory_service;

import com.microserviceDemo.inventory_service.model.Inventory;
import com.microserviceDemo.inventory_service.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}
	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
			Inventory inventory0=new Inventory();
			inventory0.setSkuCode("iphone_13_pro");
			inventory0.setQuantity(String.valueOf(105));
			Inventory inventory1=new Inventory();
			inventory1.setSkuCode("iphone_13_red");
			inventory1.setQuantity(String.valueOf(0));
			inventoryRepository.save(inventory0);
			inventoryRepository.save(inventory1);
		};
	}

}
