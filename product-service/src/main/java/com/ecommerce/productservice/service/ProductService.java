package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductRequest;
import com.ecommerce.productservice.dto.ProductResponse;
import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<ProductResponse> getAllProducts(){
        List<Product> products = prodRepo.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product prod){
        return ProductResponse.builder()
                .id(prod.getId())
                .name(prod.getName())
                .description(prod.getDescription())
                .price(prod.getPrice())
                .build();
    }

    public void deleteProductById(String Id){
        prodRepo.deleteById(Id);
    }
}
