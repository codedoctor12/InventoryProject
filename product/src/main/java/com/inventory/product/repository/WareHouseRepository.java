package com.inventory.product.repository;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventory.product.model.Product;
import com.inventory.product.model.WareHouse;

@Repository
public interface WareHouseRepository extends JpaRepository<WareHouse,Long> {

    public WareHouse findByname(String s);
}
