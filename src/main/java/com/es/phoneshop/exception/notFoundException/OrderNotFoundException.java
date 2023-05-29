package com.es.phoneshop.exception.notFoundException;

public class OrderNotFoundException extends EntityNotFoundException {

    public OrderNotFoundException(Long id) {
        super(id, "Order");
    }
}
