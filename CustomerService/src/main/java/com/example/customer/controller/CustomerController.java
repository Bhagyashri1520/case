package com.example.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.customer.entity.Customer;
import com.example.customer.exception.CustomerNotFoundException;
import com.example.customer.security.JwtUtil;
import com.example.customer.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private JwtUtil jwtUtil;

    public String createJwt(String username) {
        return jwtUtil.generateToken(username);
    }

    public boolean isValidJwt(String token, String username) {
        return jwtUtil.validateToken(token, username);
    }


    // Create customer
    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {
        // Create a customer via the service
        Customer createdCustomer = customerService.createCustomer(customer);
        // Return a success message with the created customer information
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body("Customer successfully created: " + createdCustomer.getName());
    }

    // Get customer by id
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(null); // Or use a custom error message
        }
    }

    // Update customer
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        if (updatedCustomer != null) {
            return ResponseEntity.ok("Customer successfully updated: " + updatedCustomer.getName());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Customer not found with id: " + id);
        }
    }

    // Delete customer

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id); // If no exception is thrown, deletion was successful
            return ResponseEntity.status(HttpStatus.OK)
                                 .body("Customer successfully deleted with id: " + id);
        } catch (CustomerNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(ex.getMessage()); // Return exception message if customer is not found
        }
    }



    // List all customers (Optional)
    @GetMapping
    public ResponseEntity<Iterable<Customer>> getAllCustomers() {
        Iterable<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
}
