package com.inventory.product.controller;

import java.util.ArrayList;
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
import com.inventory.product.dto.ProductResponse;
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
    public String displayText(Model model)
     {
       List<Product> products  = productRepository.findAll();

    //    model.addAttribute("products", products); 
    //    model.addAttribute("product", new Product());
       List<WareHouse> entities  = wareHouseRepository.findAll();
       model.addAttribute("warehouses", entities); 
       model.addAttribute("warehouse", new WareHouse());
       System.out.println(entities);
    //    WareHouse wareHouse = new WareHouse();
    //    WareHouse wareHouse2 = new WareHouse();

    //    wareHouse.setName("Main");
    //    wareHouse.setLocation("California");
    //    wareHouse2.setName("South");
    //    wareHouse2.setLocation("New York");
    //    wareHouseRepository.save(wareHouse);
    //    wareHouseRepository.save(wareHouse2);

    //    List<WareHouse> war = wareHouseRepository.findAll();
    //    model.addAttribute("warehouses", war); 
    //    model.addAttribute("warehouse", new WareHouse());
    //    ModelAndView modelAndView = new ModelAndView();
    //     modelAndView.setViewName("index");
        return "wareHouse";

        
         // Thymeleaf template name
    }
    @RequestMapping("/index/{id}")
    public String getWareHouseProducts(@PathVariable Long id, Model model,RedirectAttributes redirectAttributes)
    {
        ArrayList<ProductResponse> fProducts = new ArrayList<>();
        List<ProductResponse> foundProduct = productService.getAllProducts();
        for(ProductResponse prod : foundProduct)
        {
            if(prod.getWareHouse().getId().equals(id))
            {
                fProducts.add(prod);
            }
        }
        
        redirectAttributes.addFlashAttribute("foundProduct", fProducts); 
        model.addAttribute("foundProduct", redirectAttributes);
        return "index";
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
    public String searchProduct(@ModelAttribute("product") ProductRequest product, RedirectAttributes redirectAttributes, Model model) 
    { 
        String searchBy = product.getCategory();
        // System.out.println("Search By: "+searchBy);
        String [] inputs = searchBy.split(",");
        // System.out.println("Categroy "+inputs[0]+ " Text Input "+inputs[1]);
        ArrayList<ProductResponse> fProducts = new ArrayList<>();
        List<ProductResponse> foundProduct = productService.getAllProducts();
        for(ProductResponse prod : foundProduct)
        {
            if(prod.getCategory().equals(inputs[0])||prod.getName().equals(inputs[0]))
            {
                fProducts.add(prod);
                
            }
        }
        // Long l_id = Long.parseLong(id);
        // List<Product> foundProduct = productRepository.findAll();
        // for(Product product:foundProduct)
        // {
        //     if(product.getId()==l_id)
        //     {
        //        
        //     }
        // }
       
        //     redirectAttributes.addFlashAttribute("notFound", true);
        //     model.addAttribute("notFound", redirectAttributes);
        redirectAttributes.addFlashAttribute("foundProduct", fProducts); 
        model.addAttribute("foundProduct", redirectAttributes);
        return "redirect:/";

       
        
    }
    @GetMapping("/products/{id}/edit")
    public String editPerson(@PathVariable Long id, Model model) {
        ProductResponse optionalProduct = productService.findById(id);
        if (optionalProduct!=null) {
            model.addAttribute("product", optionalProduct);
            return "edit";
        } else {
            return "redirect:/inventory";
        }
    }
    @SuppressWarnings("null")
    @PostMapping("/products/{id}/updateProduct")
    public String updateProduct(@PathVariable Long id, @ModelAttribute("product") ProductRequest updatedProduct) {
        @SuppressWarnings("null")
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent())
        {
           productService.updateProduct(updatedProduct, id);

        }
        else
        {
            return "product Not found!";
        }
        return "redirect:/";
    }
    @RequestMapping("/wareHouses")
    public String wareHouseView(Model model)
    {
        List<WareHouse> entities  = wareHouseRepository.findAll();
       model.addAttribute("warehouses", entities); 
       model.addAttribute("warehouse", new WareHouse());

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
