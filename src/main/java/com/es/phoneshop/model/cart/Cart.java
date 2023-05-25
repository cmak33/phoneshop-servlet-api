package com.es.phoneshop.model.cart;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Cart implements Serializable {

    private final List<CartItem> cartItems;
    private int totalQuantity;
    private BigDecimal totalCost;

    public Cart() {
        cartItems = new ArrayList<>();
    }

    public void addItem(CartItem item) {
        cartItems.add(item);
    }
}
