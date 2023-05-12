package com.es.phoneshop.exception;

public class OutOfStockException extends Exception {

    public OutOfStockException(int quantity, int availableQuantity) {
        super(String.format("Wanted quantity of %d is more than " +
                "available quantity of %d", quantity, availableQuantity));
    }
}
