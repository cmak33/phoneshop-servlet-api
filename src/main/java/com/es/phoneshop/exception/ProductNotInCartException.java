package com.es.phoneshop.exception;

public class ProductNotInCartException extends RuntimeException {

    public ProductNotInCartException(Long id) {
        super(String.format("Product with id %d was not in cart", id));
    }
}
