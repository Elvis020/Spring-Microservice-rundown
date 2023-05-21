package com.programmingwithtechie.orderservice.service;

import com.programmingwithtechie.orderservice.dto.InventoryResponse;
import com.programmingwithtechie.orderservice.dto.OrderLineItemsRequest;
import com.programmingwithtechie.orderservice.dto.OrderRequest;
import com.programmingwithtechie.orderservice.model.Order;
import com.programmingwithtechie.orderservice.model.OrderLineItems;
import com.programmingwithtechie.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest
                .getOrderLineItemsRequests()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order
                .getOrderLineItemsList()
                .stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        // Call inventory service to check if product is in stock
        InventoryResponse[] inventoryResponseArray = webClientBuilder
                .build()
                .get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block(); // makes sync req

        boolean allProductsInStock = Arrays
                .stream(inventoryResponseArray)
                .allMatch(InventoryResponse::getIsInStock);
        if(!allProductsInStock){
            throw  new IllegalArgumentException("Product is not in stock, please try again later...");
        }
        orderRepository.save(order);
    }


    public OrderLineItems mapToDto(OrderLineItemsRequest orderLineItemsRequest) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsRequest.getPrice());
        orderLineItems.setQuantity(orderLineItemsRequest.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsRequest.getSkuCode());
        return orderLineItems;
    }
}
