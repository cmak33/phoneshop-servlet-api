package com.es.phoneshop.exception;

public class ProductNotFoundException extends EntityNotFoundException {

    public ProductNotFoundException(Long id) {
        super(id, "Product");
    }
}
