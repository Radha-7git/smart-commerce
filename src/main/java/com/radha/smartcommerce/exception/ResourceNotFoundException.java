package com.radha.smartcommerce.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException (String message) {
        super (message);
    }
}
