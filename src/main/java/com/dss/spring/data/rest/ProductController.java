package com.dss.spring.data.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;
@Controller
@RequestMapping("products")
public class ProductController {
	 	@Autowired
	 	private ProductService productService;
	 	
	 	@GetMapping
	     public String getAllProducts(@RequestParam(value = "search", required = false) String search, Model model) {
	         List<Product> products;
	         
	         if (search != null && !search.isEmpty()) {
	             // Si hay una búsqueda, filtrar los productos por nombre
	             products = productService.getProductsByName(search);
	         } else {
	             // Si no hay búsqueda, obtener todos los productos
	             products = productService.getAllProducts();
	         }
	         
	         model.addAttribute("products", products);
	         model.addAttribute("search", search); // Guardar el valor de búsqueda para que se mantenga en la barra de búsqueda
	         return "products";
	     }
	 	@PostMapping("/add")
	 	public String addProduct(@ModelAttribute Product product) {
	 		
	 		productService.saveProduct(product);
	 		return "redirect:/admin";
	 	}
	 	
	 	
	 	
	 	@PostMapping("/edit/{id}")
	 	public String editProduct(@PathVariable Long id,@RequestParam String name,@RequestParam double price) {
	 		Product product=productService.getProductById(id);
	 		if(product !=null) {
		 		product.setName(name);
		 		product.setPrice(price);
		 		productService.saveProduct(product);
	 		}
	 		return "redirect:/admin";
	 	}
	 	@PostMapping("/delete/{id}")
	 	public String deleteProduct(@PathVariable Long id) {
	 		productService.deleteProduct(id);
	 		return "redirect:/admin";
	 	}
}
