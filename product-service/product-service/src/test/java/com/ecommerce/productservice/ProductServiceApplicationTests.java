package com.ecommerce.productservice;

import com.ecommerce.productservice.dto.ProductRequest;
import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.repository.ProductRepository;
import com.ecommerce.productservice.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ProductServiceApplicationTests {

	// To be able to make a request from the test, we use mockMvc which will provide a mock servlet environment to call controller endpoints
	@Autowired
	private MockMvc mockMvc;

	//converts JSON to pojo and vice-versa
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository prodRepo;

	@Autowired
	private ProductService prodService;

	//This container method will only take objects of type string
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	//the replica uri will be added dynamically to the spring contest while running the test
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void testCreateProduct() throws Exception {
		ProductRequest prodRequest = getProductRequest();
		String productRequest = objectMapper.writeValueAsString(prodRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/ecommerce/api/product/add")
					.contentType(MediaType.APPLICATION_JSON)
					.content(productRequest))
				.andExpect(status().isCreated());
		Assertions.assertEquals(1, prodRepo.findAll().size());
	}

	private ProductRequest getProductRequest(){
		return ProductRequest.builder()
				.name("Iphone 13")
				.description("Iphone 13")
				.price(BigDecimal.valueOf(1200))
				.build();
	}

	@Test
	void getProduct(){
		addProduct();
		List<Product> productList = prodRepo.findAll();
		for(Product product: productList){
			Assertions.assertEquals("Iphone 13", product.getName());
		}
	}

	void addProduct(){
		ProductRequest productRequest = ProductRequest.builder()
				.name("Iphone 13")
				.description("Iphone 13")
				.price(BigDecimal.valueOf(1200))
				.build();
		prodService.createProduct(productRequest);
	}

}
