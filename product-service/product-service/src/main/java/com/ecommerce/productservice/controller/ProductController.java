package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.ProductRequest;
import com.ecommerce.productservice.dto.ProductResponse;
import com.ecommerce.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ecommerce/api/product")
public class ProductController {

    @Autowired
    private ProductService prodService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String createProduct(@RequestBody ProductRequest prodRequest){
        prodService.createProduct(prodRequest);
        return "Product added successfully.";
    }

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts(){
        return prodService.getAllProducts();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteById(@PathVariable("id") String productId){
        prodService.deleteProductById(productId);
        return "Product deleted successfully.";
    }
}
