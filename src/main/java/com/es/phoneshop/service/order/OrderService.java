package com.es.phoneshop.service.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;

public interface OrderService {

    Order createOrder(Cart cart);

    Order getOrder(Long id);

    void placeOrder(Order order);
}
