package com.es.phoneshop.service.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartProduct;
import com.es.phoneshop.model.order.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(Cart cart);

    List<CartProduct> getOrderProducts(Order order);

    Order getOrder(Long id);

    void placeOrder(Order order);
}
