package com.example.purehealthyeats.config;

import com.example.purehealthyeats.model.Product;
import com.example.purehealthyeats.model.Role;
import com.example.purehealthyeats.model.User;
import com.example.purehealthyeats.repository.ProductRepository;
import com.example.purehealthyeats.repository.RoleRepository;
import com.example.purehealthyeats.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Starting Pure Healthy Eats Application...");
        
        // Initialize roles
        initializeRoles();
        
        // Initialize admin user
        initializeAdminUser();
        
        // Initialize sample products
        initializeSampleProducts();
        
        System.out.println("✅ Application initialized successfully!");
        System.out.println("🌐 Access the application at: http://localhost:8080");
        System.out.println("👤 Admin Login: admin@purehealthyeats.com / admin123");
    }
    
    private void initializeRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("USER"));
            roleRepository.save(new Role("ADMIN"));
            System.out.println("✅ Roles created successfully!");
        }
    }
    
    private void initializeAdminUser() {
        if (!userRepository.existsByEmail("admin@purehealthyeats.com")) {
            User admin = new User();
            admin.setEmail("admin@purehealthyeats.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setPhone("+1-555-123-4567");
            admin.setAddress("123 Admin Street, Admin City, AC 12345");
            
            Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();
            Role userRole = roleRepository.findByName("USER").orElseThrow();
            admin.setRoles(Set.of(adminRole, userRole));
            
            userRepository.save(admin);
            System.out.println("✅ Admin user created successfully!");
        }
    }
    
    private void initializeSampleProducts() {
        if (productRepository.count() == 0) {
            Product[] products = {
                createProduct("Quinoa Buddha Bowl", 
                    "Nutritious quinoa bowl with fresh vegetables, avocado, and tahini dressing. Packed with protein and fiber.", 
                    new BigDecimal("299.00"), "Bowls", 420, 
                    "Quinoa, Mixed vegetables, Avocado, Tahini, Lemon, Olive oil",
                    "High in protein, fiber, and healthy fats. Vegan and gluten-free."),
                
            createProduct("Grilled Salmon Salad", 
                "Fresh Atlantic salmon grilled to perfection with mixed greens, cherry tomatoes, and balsamic vinaigrette.", 
                new BigDecimal("449.00"), "Salads", 380, 
                "Atlantic salmon, Mixed greens, Cherry tomatoes, Cucumber, Balsamic vinaigrette",
                "Rich in omega-3 fatty acids and lean protein. Low carb option."),
                
            createProduct("Green Power Smoothie", 
                "Energizing blend of spinach, kale, banana, mango, and coconut water. Perfect post-workout drink.", 
                new BigDecimal("179.00"), "Beverages", 180, 
                "Spinach, Kale, Banana, Mango, Coconut water, Chia seeds",
                "Packed with vitamins, minerals, and antioxidants. Natural energy booster."),
                
            createProduct("Mediterranean Chicken Wrap", 
                "Grilled chicken breast with hummus, vegetables, and tzatziki in a whole wheat wrap.", 
                new BigDecimal("249.00"), "Wraps", 450, 
                "Chicken breast, Whole wheat tortilla, Hummus, Cucumber, Tomatoes, Tzatziki",
                "High protein, balanced macronutrients. Mediterranean diet approved."),
                
            createProduct("Acai Superfood Bowl", 
                "Antioxidant-rich acai berries topped with granola, fresh berries, and honey drizzle.", 
                new BigDecimal("329.00"), "Bowls", 320, 
                "Acai berries, Granola, Strawberries, Blueberries, Banana, Honey",
                "Loaded with antioxidants and natural sugars. Perfect breakfast option."),
                
            createProduct("Thai Coconut Curry", 
                "Aromatic coconut curry with mixed vegetables and jasmine rice. Mild spice level.", 
                new BigDecimal("349.00"), "Mains", 480, 
                "Coconut milk, Mixed vegetables, Jasmine rice, Thai spices, Cilantro",
                "Plant-based protein, anti-inflammatory spices. Dairy-free and vegan."),
                
            createProduct("Protein Power Bowl", 
                "High-protein bowl with grilled chicken, quinoa, black beans, and sweet potato.", 
                new BigDecimal("399.00"), "Bowls", 520, 
                "Grilled chicken, Quinoa, Black beans, Sweet potato, Spinach, Lime dressing",
                "35g protein per serving. Perfect for muscle building and recovery."),
                
            createProduct("Fresh Fruit Parfait", 
                "Layers of Greek yogurt, seasonal fruits, and homemade granola.", 
                new BigDecimal("199.00"), "Desserts", 280, 
                "Greek yogurt, Seasonal fruits, Homemade granola, Honey",
                "Probiotics for gut health, natural sweetness. High in calcium and protein.")
        };
        
        for (Product product : products) {
            productRepository.save(product);
        }
        
        System.out.println("✅ Sample products created successfully!");
        }
    }
    
    private Product createProduct(String name, String description, BigDecimal price, 
                                String category, Integer calories, String ingredients, String nutritionInfo) {
        Product product = new Product(name, description, price, category);
        product.setAvailable(true);
        product.setCalories(calories);
        product.setIngredients(ingredients);
        product.setNutritionInfo(nutritionInfo);
        product.setImageUrl("/placeholder.svg?height=300&width=400&text=" + name.replace(" ", "+"));
        return product;
    }
}
