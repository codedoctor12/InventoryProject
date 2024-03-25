package com.inventory.product.repository;

import java.util.List;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventory.product.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> 
{
    
}

