package com.example.purehealthyeats.controller;

import com.example.purehealthyeats.model.Product;
import com.example.purehealthyeats.model.User;
import com.example.purehealthyeats.service.ProductService;
import com.example.purehealthyeats.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String home(Model model) {
        List<Product> featuredProducts = productService.getAvailableProducts();
        // Limit to 6 products for homepage
        if (featuredProducts.size() > 6) {
            featuredProducts = featuredProducts.subList(0, 6);
        }
        model.addAttribute("products", featuredProducts);
        return "index";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        // Get user details
        String email = authentication.getName();
        Optional<User> userOpt = userService.findByEmail(email);
        String displayName = userOpt.map(User::getFirstName).orElse("User");
        
        model.addAttribute("username", displayName);
        
        List<Product> products = productService.getAvailableProducts();
        model.addAttribute("products", products);
        
        List<String> categories = productService.getAllCategories();
        model.addAttribute("categories", categories);
        
        // Add statistics
        model.addAttribute("totalProducts", productService.getAvailableProductsCount());
        
        return "dashboard";
    }
    
    @GetMapping("/checkout")
    public String checkout(@RequestParam(required = false) String productId,
                          @RequestParam(required = false) String productName,
                          @RequestParam(required = false) String productPrice,
                          Authentication authentication,
                          Model model) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        // Get user details
        String email = authentication.getName();
        Optional<User> userOpt = userService.findByEmail(email);
        String displayName = userOpt.map(User::getFirstName).orElse("User");
        
        model.addAttribute("username", displayName);
        
        // If specific product is selected
        if (productId != null) {
            try {
                Long id = Long.parseLong(productId);
                productService.getProductById(id).ifPresent(product -> {
                    model.addAttribute("selectedProduct", product);
                    model.addAttribute("productName", product.getName());
                    model.addAttribute("productPrice", product.getPrice());
                });
            } catch (NumberFormatException e) {
                // Invalid product ID, continue without selected product
            }
        } else if (productName != null && productPrice != null) {
            // Direct product info passed
            model.addAttribute("productName", productName);
            model.addAttribute("productPrice", productPrice);
        }
        
        return "checkout";
    }
    
    @GetMapping("/menu")
    public String menu(@RequestParam(required = false) String category,
                      @RequestParam(required = false) String search,
                      Model model) {
        
        List<Product> products;
        
        if (search != null && !search.trim().isEmpty()) {
            products = productService.searchProducts(search);
            model.addAttribute("searchQuery", search);
        } else if (category != null && !category.trim().isEmpty()) {
            products = productService.getProductsByCategory(category);
            model.addAttribute("selectedCategory", category);
        } else {
            products = productService.getAvailableProducts();
        }
        
        model.addAttribute("products", products);
        model.addAttribute("categories", productService.getAllCategories());
        
        return "menu";
    }
}
