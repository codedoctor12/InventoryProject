package com.inventory.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
         .id(product.getId())
         .name(product.getName())
         .price(product.getPrice())
         .quantity(product.getQuantity())
         .category(product.getCategory())
         .rating(product.getRating())
         .sn(product.getSn())
         .build();

        return productResponse;


    }
    public ProductResponse findById(Long id)
    {
        Optional <Product> product = productRepository.findById(id);
        if(product.isPresent())
        {
            ProductResponse productResponse = ProductResponse.builder()
                .id(product.get().getId())
                .name(product.get().getName())
                .category(product.get().getCategory())
                .price(product.get().getPrice())
                .rating(product.get().getRating())
                .sn(product.get().getSn())
                .wareHouse(product.get().getWareHouse())
                .quantity(product.get().getQuantity())
                .build();
            return productResponse;
        }
        else
        {
            return null;
        }
    }
    public void updateProduct(ProductRequest productRequest,Long id)
    {
        
        Product product = Product.builder()
        .id(id)
        .name(productRequest.getName())
        .category(productRequest.getCategory())
        .price(productRequest.getPrice())
        .rating(productRequest.getRating())
        .quantity(productRequest.getQuantity())
        .sn(productRequest.getSn())
        .build();
    productRepository.save(product);
    }

}
