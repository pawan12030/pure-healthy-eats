package com.example.purehealthyeats.controller;

import com.example.purehealthyeats.model.User;
import com.example.purehealthyeats.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       Model model) {

        if (error != null) {
            model.addAttribute("error", "Invalid email or password. Please try again.");
        }

        if (logout != null) {
            model.addAttribute("success", "You have been logged out successfully.");
        }

        return "auth/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                              BindingResult result,
                              @RequestParam String confirmPassword,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        System.out.println("Registration attempt for email: " + user.getEmail());
        
        // Validate password confirmation
        if (!user.getPassword().equals(confirmPassword)) {
            result.rejectValue("password", "error.user", "Passwords do not match");
            System.out.println("Password confirmation failed");
        }

        // Check if user already exists
        if (userService.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "An account with this email already exists");
            System.out.println("User already exists with email: " + user.getEmail());
        }

        // Return to form if there are validation errors
        if (result.hasErrors()) {
            System.out.println("Validation errors found: " + result.getAllErrors());
            return "auth/register";
        }

        try {
            // Initialize roles if needed
            userService.initializeRoles();
            
            // Create the user
            User savedUser = userService.createUser(user);
            System.out.println("User created successfully with ID: " + savedUser.getId());
            
            redirectAttributes.addFlashAttribute("success", 
                "Registration successful! Please login with your credentials.");
            return "redirect:/login";
            
        } catch (Exception e) {
            System.err.println("Registration failed: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout=true";
    }
} // <-- This closing brace was missing!