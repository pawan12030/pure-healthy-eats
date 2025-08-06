package com.example.purehealthyeats.service;

import com.example.purehealthyeats.model.Product;
import com.example.purehealthyeats.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public List<Product> getAvailableProducts() {
        return productRepository.findByAvailableTrue();
    }
    
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryAndAvailableTrue(category);
    }
    
    public List<Product> searchProducts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAvailableProducts();
        }
        return productRepository.searchProducts(query.trim());
    }
    
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }
    
    public List<String> getAllCategories() {
        return productRepository.findDistinctCategories();
    }
    
    public List<Product> getLatestProducts() {
        return productRepository.findLatestProducts();
    }
    
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }
    
    public Long getAvailableProductsCount() {
        return productRepository.countAvailableProducts();
    }
    
    public void toggleProductAvailability(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setAvailable(!product.getAvailable());
            productRepository.save(product);
        }
    }
}
