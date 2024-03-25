package com.inventory.product.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.inventory.product.repository.*;
import com.inventory.product.dto.ProductRequest;
import com.inventory.product.model.Product;
import com.inventory.product.dto.ProductResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService
{
    private final ProductRepository productRepository;
    @SuppressWarnings("null")
    public void saveNewProduct(ProductRequest productRequest)
    {
        Product product = Product.builder()
        .name(productRequest.getName())
        .price(productRequest.getPrice())
        .quantity(productRequest.getQuantity())
        .category(productRequest.getCategory())
        .rating(productRequest.getRating())
        .sn(productRequest.getSn())
        .build();
       
        productRepository.save(product);
        
    }
    public List<ProductResponse> getAllProducts()
    {
        List<Product> products = new ArrayList<>();
       products = productRepository.findAll();
        return products.stream().map(this::mapToResponse).toList();

    }
    public ProductResponse mapToResponse(Product product)
    {
         ProductResponse productResponse = ProductResponse.builder()
         .name(product.getName())
         .price(product.getPrice())
         .quantity(product.getQuantity())
         .category(product.getCategory())
         .rating(product.getRating())
         .sn(product.getSn())
         .build();

        return productResponse;


    }
    public void updateProduct(ProductRequest productRequest)
    {
        
    }

}
