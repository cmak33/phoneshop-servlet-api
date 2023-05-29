package com.es.phoneshop.exception;

public class NegativeStockException extends RuntimeException {

    public NegativeStockException() {
        super("Product stock cannot be negative");
    }
}
