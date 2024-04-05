package com.inventory.product.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.inventory.product.dto.ProductRequest;
import com.inventory.product.dto.ProductResponse;
import com.inventory.product.dto.WareHouseDTO;
import com.inventory.product.model.Product;
import com.inventory.product.model.WareHouse;
import com.inventory.product.repository.ProductRepository;
import com.inventory.product.repository.WareHouseRepository;
import com.inventory.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/data")
@CrossOrigin("http://127.0.0.1:5500/") 
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    

    
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final WareHouseRepository wareHouseRepository;
    private WareHouse wareHouse;
    
    @GetMapping
    public ResponseEntity<List<WareHouseDTO>> displayText(Model model)
     {
       List<WareHouseDTO> products  = productService.getWareHouses();
       System.out.println(products);
        return  new ResponseEntity<List<WareHouseDTO>>(products, HttpStatus.OK);

        
        
    }
    @GetMapping("/products/{id}")
    public ResponseEntity<List<ProductResponse>> getProducts(@PathVariable("id") String id)
     {

        Long l_id = Long.parseLong(id);
       Optional<WareHouse> wareHouse = wareHouseRepository.findById(l_id);
       if(wareHouse.isPresent())
       {
            this.wareHouse=wareHouse.get();
            List<ProductResponse> products = productService.converToResponse(wareHouse.get().getProductList());
            return  new ResponseEntity<List<ProductResponse>>(products, HttpStatus.OK);
       }
       else
       {
         return null;
       }
        

        
        
    }

    @PostMapping("/newProduct")
    public ResponseEntity<ProductRequest> saveProduct(@RequestBody ProductRequest product, RedirectAttributes redirectAttributes)
    {
        String cap = this.wareHouse.getCapacity();
        int capacity = Integer.parseInt(cap);
        if(capacity==this.wareHouse.getProductList().size())
        {
            return null;
        }
        String productId = "PD" + (1000 + new Random().nextInt(9000)); 
        product.setSn(productId);
        System.out.println(product);
        productService.saveNewProduct(product,this.wareHouse);
        redirectAttributes.addFlashAttribute("insertSuccess", true); 

         return new ResponseEntity<ProductRequest>(product,HttpStatus.OK); 
    }
  
    @DeleteMapping(path = "/{id}")
   public String deleteProduct(@PathVariable("id") String id,Model model) {
        System.out.println("here!--------------");
        Long l_Id = Long.parseLong(id);
        productRepository.deleteById(l_Id);
        return "redirect:/inventory"; 
    }
    @GetMapping("/searchProduct/{id}") 
    public ResponseEntity<List<ProductResponse>> searchProduct(@PathVariable("id") String sKeyWord, RedirectAttributes redirectAttributes, Model model) 
    { 
        List<Product> products = new ArrayList<Product>();
  
        for(Product p : this.wareHouse.getProductList()){

            if(p.getName().startsWith(sKeyWord))
            {
                products.add(p);
            }
        }
        return  new ResponseEntity<List<ProductResponse>>(productService.converToResponse(products), HttpStatus.OK);

    }
    @PostMapping("/products/{id}/updateProduct")
    public ResponseEntity<List<ProductRequest>> updateProduct(@PathVariable String id, @RequestBody ProductRequest updatedProduct)
     {
        if(id==null)
        {

            

        }

        Long lId = Long.parseLong(id);
        Optional<Product> optionalProduct = productRepository.findById(lId);
        List<ProductRequest> prodR= new ArrayList<>();
        if(optionalProduct.isPresent())
        {
            System.out.println(updatedProduct);
            productService.updateProduct(updatedProduct, lId,optionalProduct.get().getSn(),this.wareHouse);
            prodR.add(updatedProduct);
           return new ResponseEntity<List<ProductRequest>>(prodR,HttpStatus.OK);

        }
        else
        {
            return  null;
        }
    }
    @SuppressWarnings("null")
    @DeleteMapping("/product")
    public ResponseEntity<Product> deleteMovie(@RequestBody Product product ) {
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
        
    }

}
