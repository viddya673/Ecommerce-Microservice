package com.ecommerce.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class webClientConfig {

    @Bean
    public WebClient webClient(){
        // to return a bean of type WebClient
        // so the bean will be create with the name webClient() (func)
        return WebClient.builder().build();
    }
}
