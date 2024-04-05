package com.inventory.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.inventory.product.repository.*;
import com.inventory.product.dto.ProductRequest;
import com.inventory.product.model.Product;
import com.inventory.product.model.WareHouse;
import com.inventory.product.dto.ProductResponse;
import com.inventory.product.dto.WareHouseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService
{
    private final ProductRepository productRepository;
    private List<Product> producs;
    private final WareHouseRepository wareHouseRepistory;


    public List<WareHouseDTO> getWareHouses()
    {
        List<WareHouse> wareHouses = wareHouseRepistory.findAll();
       return  wareHouses.stream().map(this::mapToWareHouses).toList();
        

    } 
    public WareHouseDTO mapToWareHouses(WareHouse wareHouse)
    {
        WareHouseDTO  wareHouseDTO = WareHouseDTO.builder()
            .id(wareHouse.getId())
            .name(wareHouse.getName())
            .capacity(wareHouse.getCapacity())
            .location(wareHouse.getLocation())
            .build();
        return wareHouseDTO;
    }

    @SuppressWarnings("null")
    public void saveNewProduct(ProductRequest productRequest,WareHouse wareHouse)
    { 
    Product product = new Product();
         product = Product.builder()
        .name(productRequest.getName())
        .price(productRequest.getPrice())
        .quantity(productRequest.getQuantity())
        .category(productRequest.getCategory())
        .rating(productRequest.getRating())
        .sn(productRequest.getSn())
        .warehouse(wareHouse)
        .build();
       
       Product prodId = productRepository.save(product);
       productRequest.setId(prodId.getId());
        
    }
    public List<ProductResponse> converToResponse(List<Product> products)
    {
        return products.stream().map(this::mapToResponse).toList();
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
                // .wareHouse(product.get().getWareHouse())
                .quantity(product.get().getQuantity())
                .build();
            return productResponse;
        }
        else
        {
            return null;
        }
    }
    @SuppressWarnings("null")
    public void updateProduct(ProductRequest productRequest,Long id,String sn,WareHouse wareHouse)
    {
        System.out.println("What Iam looking for: "+productRequest);
        WareHouse l = new WareHouse();
        l.setId(wareHouse.getId());
        l.setCapacity(wareHouse.getCapacity());
        l.setLocation(wareHouse.getLocation());
        l.setName(wareHouse.getName());
        Product product = Product.builder()
        .id(id)
        .name(productRequest.getName())
        .category(productRequest.getCategory())
        .price(productRequest.getPrice())
        .rating(productRequest.getRating())
        .quantity(productRequest.getQuantity())
        .sn(sn)
        .warehouse(l)
        .build();
    productRepository.save(product);
    }
    public List<Product> getProducts()
    {
        return this.producs;
    }
    public void setProducts(List<Product> products)
    {
         this.producs=products;
    }
    public List<ProductResponse> findProductByName(String s)
    {
        List<Product> products = productRepository.findProductByName(s);
        return products.stream().map(this::mapToNewResponse).toList();
    }
    public ProductResponse mapToNewResponse(Product product)
    {
        ProductResponse productR = ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .category(product.getCategory())
            .price(product.getPrice())
            .sn(product.getSn())
            .quantity(product.getQuantity())
            .rating(product.getRating())
            .build();
        return productR;

    }

}
