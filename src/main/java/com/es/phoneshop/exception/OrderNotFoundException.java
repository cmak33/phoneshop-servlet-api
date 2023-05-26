package com.es.phoneshop.exception;

public class OrderNotFoundException extends EntityNotFoundException{

    public OrderNotFoundException(Long id) {
        super(id, "Order");
    }
}
