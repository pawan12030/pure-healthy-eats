package com.example.purehealthyeats.repository;

import com.example.purehealthyeats.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByAvailableTrue();
    List<Product> findByCategory(String category);
    List<Product> findByCategoryAndAvailableTrue(String category);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.available = true ORDER BY p.category")
    List<String> findDistinctCategories();
    
    @Query("SELECT p FROM Product p WHERE p.available = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Product> searchProducts(@Param("keyword") String keyword);
    
    @Query("SELECT p FROM Product p WHERE p.available = true ORDER BY p.createdAt DESC")
    List<Product> findLatestProducts();
    
    @Query("SELECT COUNT(p) FROM Product p WHERE p.available = true")
    Long countAvailableProducts();
}
