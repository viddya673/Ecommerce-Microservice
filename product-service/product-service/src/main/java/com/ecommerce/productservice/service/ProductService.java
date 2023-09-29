package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductRequest;
import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository prodRepo;

    public void createProduct(ProductRequest prodReq){

        //Creating an instance of the product to be created
        Product prod = Product.builder()
                .name(prodReq.getName())
                .description(prodReq.getDescription())
                .price(prodReq.getPrice())
                .build();
        prodRepo.save(prod);
        log.info("Product {} saved successfully.", prod.getId());
    }
}
