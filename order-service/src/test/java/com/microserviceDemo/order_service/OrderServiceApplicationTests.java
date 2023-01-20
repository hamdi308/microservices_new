package com.microserviceDemo.order_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microserviceDemo.order_service.dto.OrderLineItemsDto;
import com.microserviceDemo.order_service.dto.OrderRequest;
import com.microserviceDemo.order_service.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class OrderServiceApplicationTests {
	@Container
	static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8");
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private OrderRepository orderRepository;
	@DynamicPropertySource
	static void registerMySQLProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mysql::getJdbcUrl);
		registry.add("spring.datasource.username", mysql::getUsername);
		registry.add("spring.datasource.password", mysql::getPassword);
	}
	@Test
	void shouldPlaceOrder() throws Exception {
		OrderRequest orderRequest=getOrderRequest();
		String orderRequestString=objectMapper.writeValueAsString(orderRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8081/api/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(orderRequestString))
				.andExpect(status().isCreated());
		assertEquals(1,orderRepository.findAll().size());

	}

	private OrderRequest getOrderRequest() {
		return OrderRequest.builder().orderLineItemsDtoList(Arrays.asList(new OrderLineItemsDto("mac", new BigDecimal(3700), 1),
				new OrderLineItemsDto("hp", new BigDecimal(2700), 2),
				new OrderLineItemsDto("iphone_11", new BigDecimal(1500), 4),
				new OrderLineItemsDto("iphone_7", new BigDecimal(800), 1)))
				.build();
	}

}
