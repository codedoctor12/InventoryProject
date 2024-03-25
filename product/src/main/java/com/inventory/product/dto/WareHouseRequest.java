package com.inventory.product.dto;

import java.util.List;

import com.inventory.product.model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WareHouseRequest {
    private Long id;
    private String name;
    private String location;
    private List<Product> productList;
}
