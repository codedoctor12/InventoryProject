package com.inventory.product.dto;

import com.inventory.product.model.WareHouse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private Long id;
    private String sn;
    private String category;
    private String name;
    private String price;
    private String rating;
    private String quantity;
    private WareHouse wareHouse;

}
