package com.ecommerce.inventoryservice.service;

import com.ecommerce.inventoryservice.model.Inventory;
import com.ecommerce.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode){
        Optional<Inventory> item = inventoryRepository.findBySkuCode(skuCode);
        return item.isPresent() && item.get().getQuantity() > 0;
    }
}
