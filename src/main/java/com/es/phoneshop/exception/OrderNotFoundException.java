package com.es.phoneshop.exception;

public class OrderNotFoundException extends EntityNotFoundException {

    public OrderNotFoundException(String secureId) {
        super(String.format("Order with secure id %s was not found", secureId));
    }
}
