package com.inventory.product.controller;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.inventory.product.model.Product;
import com.inventory.product.model.WareHouse;
import com.inventory.product.repository.ProductRepository;
import com.inventory.product.repository.WareHouseRepository;
import com.inventory.product.service.ProductService;
import com.inventory.product.service.WareHouseService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @Autowired
    private final WareHouseRepository wareHouseRepository;

    
    @Autowired
    private final ProductRepository productRepository;
  
    @RequestMapping("/")
    public String displayText(Model model) {
       List<Product> entities  = productRepository.findAll();
       model.addAttribute("products", entities); 
       model.addAttribute("product", new Product());
       WareHouse wareHouse = new WareHouse();
       wareHouse.setName("Main");
       wareHouse.setLocation("California");
       wareHouseRepository.save(wareHouse);
       List<WareHouse> war = wareHouseRepository.findAll();
       model.addAttribute("warehouses", war); 
       model.addAttribute("warehouse", new WareHouse());
    //    ModelAndView modelAndView = new ModelAndView();
    //     modelAndView.setViewName("index");
        return "index";

        
         // Thymeleaf template name
    }
 
    @PostMapping("/newProduct")
    public String saveProduct(@ModelAttribute("product") ProductRequest product, RedirectAttributes redirectAttributes)
    {
        String productId = "PD" + (1000 + new Random().nextInt(9000)); 
        product.setSn(productId);
        productService.saveNewProduct(product);
        redirectAttributes.addFlashAttribute("insertSuccess", true); 
        return "redirect:/"; 
    }
  
    @DeleteMapping(path = "/{id}")
   public String deleteProduct(@PathVariable("id") String id,Model model) {
        System.out.println("here!--------------");
        Long l_Id = Long.parseLong(id);
        productRepository.deleteById(l_Id);
        return "redirect:/inventory"; 
    }
    @PostMapping("/searchProduct") 
    public String searchProduct(@RequestParam("id") String id, RedirectAttributes redirectAttributes, Model model) 
    { 
        Long l_id = Long.parseLong(id);
        List<Product> foundProduct = productRepository.findAll();
        for(Product product:foundProduct)
        {
            if(product.getId()==l_id)
            {
                redirectAttributes.addFlashAttribute("foundProduct", product); 
    
                model.addAttribute("foundProduct", redirectAttributes);
                return "redirect:/";
            }
        }
       
            redirectAttributes.addFlashAttribute("notFound", true);
            model.addAttribute("notFound", redirectAttributes);
            return "redirect:/";

       
        
    }
    @GetMapping("/products/{id}/edit")
    public String editPerson(@PathVariable Long id, Model model) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            model.addAttribute("product", optionalProduct.get());
            return "edit";
        } else {
            return "redirect:/inventory";
        }
    }
    @SuppressWarnings("null")
    @PostMapping("/products/{id}/updateProduct")
    public String updateProduct(@PathVariable Long id, @ModelAttribute("product") Product updatedProduct) {
        @SuppressWarnings("null")
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent())
        {
            Product product = optionalProduct.get().builder()
                .id(optionalProduct.get().getId())
                .name(updatedProduct.getName())
                .category(updatedProduct.getCategory())
                .price(updatedProduct.getPrice())
                .rating(updatedProduct.getRating())
                .quantity(updatedProduct.getQuantity())
                .build();
            productRepository.save(product);
            System.out.println(product);

            
        }
        return "redirect:/";
    }
    @RequestMapping("/wareHouses")
    public String wareHouseView(Model model)
    {
        List<WareHouse> entities  = wareHouseRepository.findAll();
       model.addAttribute("products", entities); 
       model.addAttribute("product", new WareHouse());

        return "wareHouse";
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<Product> updateItem(@PathVariable String id, @RequestBody Product newItemData) {
        
    // }
    // @PostMapping("/searchProduct") 
    // public String searchProduct(@RequestParam(name = "id") String id, RedirectAttributes redirectAttributes, 
    //         Model model) { 
    //     Optional<Product> foundProduct = productService.getProductById(id); 
    //     model.addAttribute("product", new Product()); 
    //     if (foundProduct.isPresent()) { 
    //         redirectAttributes.addFlashAttribute("foundProduct", foundProduct); 
    //     } else { 
    //         redirectAttributes.addFlashAttribute("notFound", true); 
    //     } 
  
    //     return "redirect:/"; 
    // } 


}
