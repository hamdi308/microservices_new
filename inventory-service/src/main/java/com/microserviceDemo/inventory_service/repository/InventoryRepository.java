package com.microserviceDemo.inventory_service.repository;

import com.microserviceDemo.inventory_service.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    List<Inventory> findBySkuCodeIn(List<String> skuCode);
}
