package com.es.phoneshop.exception;

public class OutOfStockException extends Exception {

    public OutOfStockException(long productId, int quantity, int availableQuantity) {
        super(String.format("Wanted quantity of %d is more than " +
                "available quantity of %d for product with id %d", quantity, availableQuantity, productId));
    }
}
