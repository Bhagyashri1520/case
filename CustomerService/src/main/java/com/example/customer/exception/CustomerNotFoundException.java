package com.example.customer.exception;

public class CustomerNotFoundException extends RuntimeException {
    
    public CustomerNotFoundException(String message) {
        super(message); // Pass the custom message to the parent class constructor
    }
}
