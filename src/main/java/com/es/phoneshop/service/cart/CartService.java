package com.es.phoneshop.service.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;

public interface CartService {

    Cart getCart(AttributesHolder attributesHolder);

    void addItem(Cart cart, Long productId, int quantity) throws OutOfStockException;
}
