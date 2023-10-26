package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.InventoryResponse;
import com.ecommerce.orderservice.dto.OrderLineItemsDto;
import com.ecommerce.orderservice.dto.OrderRequest;
import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.model.OrderLineItems;
import com.ecommerce.orderservice.repository.OrderRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        // call inventory service to check of items are in stock
        // .block() indicates synchronous request
        // localhost:8082 has been replaced by the name "inventory-service" as in application.properties file, to get the port in which it runs
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/ecommerce/api/inventory/in-stock",
                        uriBuilder ->uriBuilder.queryParam("sku_code", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsArraysInStock = Arrays.stream(inventoryResponseArray)
                                                    .allMatch(InventoryResponse::isInStock);

        if (allProductsArraysInStock) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product not in stock. Please try again later.");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
