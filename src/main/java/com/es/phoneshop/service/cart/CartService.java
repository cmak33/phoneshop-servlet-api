package com.es.phoneshop.service.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotInCartException;
import com.es.phoneshop.model.attributesHolder.AttributesHolder;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartProduct;

import java.util.List;

public interface CartService {

    Cart getCart(AttributesHolder attributesHolder);

    List<CartProduct> getCartProducts(AttributesHolder attributesHolder);

    void updateCartItem(AttributesHolder attributesHolder, Long id, int newQuantity) throws OutOfStockException, ProductNotInCartException;

    void deleteCartItem(AttributesHolder attributesHolder, Long id) throws ProductNotInCartException;

    void setCart(AttributesHolder attributesHolder, Cart cart);

    void addCartItem(AttributesHolder attributesHolder, Long productId, int quantity) throws OutOfStockException;
}
