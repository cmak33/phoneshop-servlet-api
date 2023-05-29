package com.es.phoneshop.exception.notFoundException;

public class ProductNotFoundException extends EntityNotFoundException {

    public ProductNotFoundException(Long id) {
        super(id, "Product");
    }
}
