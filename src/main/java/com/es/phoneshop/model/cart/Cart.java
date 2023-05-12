package com.es.phoneshop.model.cart;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private final List<CartItem> cartItems;

    public Cart() {
        cartItems = new ArrayList<>();
    }

    public void addItem(CartItem item) {
        cartItems.add(item);
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}