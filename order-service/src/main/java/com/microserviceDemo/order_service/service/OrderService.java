package com.microserviceDemo.order_service.service;

import com.microserviceDemo.order_service.dto.InventoryResponse;
import com.microserviceDemo.order_service.dto.OrderLineItemsDto;
import com.microserviceDemo.order_service.dto.OrderRequest;
import com.microserviceDemo.order_service.model.Order;
import com.microserviceDemo.order_service.model.OrderLineItems;
import com.microserviceDemo.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final WebClient webClient;
    public void placeOrder(OrderRequest orderRequest){
        Order order=new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList= orderRequest.getOrderLineItemsDtoList().stream().map(this::mapToDto).toList();
        order.setOrderLineItemsList(orderLineItemsList);
        List<String> skuCodeList= order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();
         InventoryResponse[] InventoryResponseArray=webClient.get()
                .uri("http://localhost:8082/api/inventory",uriBuilder -> uriBuilder.queryParam("skuCode",skuCodeList).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        boolean AllProductsInStock =Arrays.stream(InventoryResponseArray).allMatch(inventoryResponse -> inventoryResponse.isInStock);
        if(AllProductsInStock){
        orderRepository.save(order);
        }else {
            throw new IllegalArgumentException("Product is not in Stock,please try again later");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems=OrderLineItems.builder()
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .skuCode(orderLineItemsDto.getSkuCode())
                .build();
        return orderLineItems;
    }

}
