package com.inventory.product.repository;

import java.util.List;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.inventory.product.model.Product;

import jakarta.transaction.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> 
{
    @Query("Select  p from Product p where p.name like ?1%")
    @Modifying
    @Transactional
    public List<Product> findProductByName(String name);

    @SuppressWarnings("unchecked")
    public Product save(Product p);

    

    
}

