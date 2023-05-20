package com.es.phoneshop.service.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.attributesHolder.AttributesHolder;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartProduct;

import java.util.List;

public interface CartService {

    Cart getCart(AttributesHolder attributesHolder);

    List<CartProduct> getCartProducts(AttributesHolder attributesHolder);

    void updateItem(AttributesHolder attributesHolder, Long id, int newQuantity) throws OutOfStockException;

    void setCart(AttributesHolder attributesHolder, Cart cart);

    void addItem(AttributesHolder attributesHolder, Long productId, int quantity) throws OutOfStockException;
}
