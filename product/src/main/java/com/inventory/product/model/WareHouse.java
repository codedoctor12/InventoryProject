package com.inventory.product.model;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name="wareHouse")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WareHouse {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
   @OneToMany(fetch = FetchType.LAZY, mappedBy = "wareHouse", cascade = CascadeType.ALL)
    private List<Product> productList;
}