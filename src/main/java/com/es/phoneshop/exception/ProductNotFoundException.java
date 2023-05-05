package com.es.phoneshop.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super(String.format("Product with id %d was not found", id));
    }

    public ProductNotFoundException(String code) {
        super(String.format("Product with code %s was not found", code));
    }
}
