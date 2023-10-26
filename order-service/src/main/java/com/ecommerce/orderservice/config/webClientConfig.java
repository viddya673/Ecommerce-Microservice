package com.ecommerce.orderservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class webClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder(){
        // to return a bean of type WebClient
        // so the bean will be create with the name webClient() (func)
        return WebClient.builder();
    }
}
