package com.inventory.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.inventory.product.dto.ProductRequest;
import com.inventory.product.dto.WareHouseRequest;
import com.inventory.product.model.Product;
import com.inventory.product.model.WareHouse;
import com.inventory.product.repository.WareHouseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class WareHouseService {
    private  final WareHouseRepository wareHouseRepository;

    public void addProductToWareHouse(String wareHouseName,ProductRequest productRequest)
    {
        WareHouse wareHouse = wareHouseRepository.findByname(wareHouseName);
        List<Product> products = wareHouse.getProductList();
        Product product = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .category(productRequest.getCategory())
                .quantity(productRequest.getQuantity())
                .sn(productRequest.getSn())
                .rating(productRequest.getRating())
                .build();
        products.add(product);
        wareHouseRepository.save(wareHouse);

    }
}
