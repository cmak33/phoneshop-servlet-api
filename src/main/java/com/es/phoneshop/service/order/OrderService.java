package com.es.phoneshop.service.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.ProductAndQuantity;
import com.es.phoneshop.model.order.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(Cart cart);

    Order getOrderBySecureId(String id);

    List<ProductAndQuantity> getOrderProducts(Order order);

    void placeOrder(Order order);
}
