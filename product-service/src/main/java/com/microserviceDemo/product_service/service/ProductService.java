package com.microserviceDemo.product_service.service;

import com.microserviceDemo.product_service.dto.ProductRequest;
import com.microserviceDemo.product_service.dto.ProductResponse;
import com.microserviceDemo.product_service.model.Product;
import com.microserviceDemo.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    @Autowired
    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        Product product=Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
        log.info("product "+product.getId()+" is saved");
   }
   public List<ProductResponse> getAllProduct(){
        return (List<ProductResponse>) productRepository.findAll().stream().map(product ->{return
             ProductResponse
                    .builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .build();}
        ).toList();
   }
}
