package com.es.phoneshop.service.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartProduct;
import com.es.phoneshop.model.order.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(Cart cart);

    Order getOrderBySecureId(String id);

    List<CartProduct> getOrderProducts(Order order);

    void placeOrder(Order order);
}
