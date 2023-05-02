package com.es.phoneshop.exception;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(long id) {
        super(String.format("Product with id %d was not found",id));
    }
}
