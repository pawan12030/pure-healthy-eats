package com.example.purehealthyeats.controller;

import com.example.purehealthyeats.service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private QRCodeService qrCodeService;

    @Value("${payment.upi.merchant-id:purehealthyeats@paytm}")
    private String merchantId;

    @Value("${payment.upi.merchant-name:Pure Healthy Eats}")
    private String merchantName;

    @PostMapping("/create-stripe-intent")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createStripePaymentIntent(@RequestBody Map<String, Object> request) {
        try {
            // Simulate Stripe payment intent creation
            Map<String, Object> response = new HashMap<>();
            response.put("clientSecret", "pi_test_" + UUID.randomUUID().toString().substring(0, 16));
            response.put("status", "requires_payment_method");
            response.put("amount", request.get("amount"));
            response.put("currency", "usd");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to create payment intent: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/generate-qr")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> generateQRCode(@RequestBody Map<String, Object> request) {
        try {
            String amount = request.get("amount").toString();
            String transactionId = "TXN" + System.currentTimeMillis();
            
            String qrCodeBase64 = qrCodeService.generateUPIQRCode(merchantId, merchantName, amount, transactionId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("qrCode", "data:image/png;base64," + qrCodeBase64);
            response.put("transactionId", transactionId);
            response.put("upiId", merchantId);
            response.put("amount", amount);
            response.put("merchantName", merchantName);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Failed to generate QR code: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/verify-payment")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verifyPayment(@RequestBody Map<String, Object> request) {
        try {
            // Simulate payment verification
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("status", "completed");
            response.put("transactionId", request.get("transactionId"));
            response.put("verified", true);
            response.put("timestamp", LocalDateTime.now().toString());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Payment verification failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/process-payment")
    public String processPayment(@RequestParam String cardNumber,
                                @RequestParam String expiryDate,
                                @RequestParam String cvv,
                                @RequestParam String cardName,
                                @RequestParam BigDecimal amount,
                                @RequestParam(required = false) String productName,
                                @RequestParam(required = false) String customerEmail,
                                Model model) {
        
        try {
            // Validate card details (basic validation)
            if (cardNumber == null || cardNumber.length() < 16) {
                model.addAttribute("error", "Invalid card number");
                return "checkout";
            }
            
            if (cvv == null || cvv.length() < 3) {
                model.addAttribute("error", "Invalid CVV");
                return "checkout";
            }
            
            // Generate order details
            String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
            
            // Calculate delivery time (2 hours from now)
            LocalDateTime orderTime = LocalDateTime.now();
            LocalDateTime deliveryTime = orderTime.plusHours(2);
            
            // Add order details to model
            model.addAttribute("orderId", orderId);
            model.addAttribute("transactionId", transactionId);
            model.addAttribute("amount", amount);
            model.addAttribute("productName", productName != null ? productName : "Healthy Meal");
            model.addAttribute("customerEmail", customerEmail);
            model.addAttribute("orderDate", orderTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
            model.addAttribute("estimatedDelivery", deliveryTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
            
            // Generate tracking QR code
            String trackingQR = qrCodeService.generateOrderTrackingQR(orderId, "https://purehealthyeats.com/track");
            model.addAttribute("trackingQR", "data:image/png;base64," + trackingQR);
            
            return "payment-success";
            
        } catch (Exception e) {
            model.addAttribute("error", "Payment processing failed: " + e.getMessage());
            return "checkout";
        }
    }
    
    @GetMapping("/success")
    public String paymentSuccess(@RequestParam(required = false) String orderId,
                                @RequestParam(required = false) String amount,
                                @RequestParam(required = false) String product,
                                Model model) {
        
        // If no parameters provided, show generic success
        if (orderId == null) {
            orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
        
        model.addAttribute("orderId", orderId);
        model.addAttribute("transactionId", "TXN-" + System.currentTimeMillis());
        model.addAttribute("amount", amount != null ? amount : "299.00");
        model.addAttribute("productName", product != null ? product : "Healthy Meal");
        model.addAttribute("orderDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
        model.addAttribute("estimatedDelivery", LocalDateTime.now().plusHours(2).format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
        
        return "payment-success";
    }
}
