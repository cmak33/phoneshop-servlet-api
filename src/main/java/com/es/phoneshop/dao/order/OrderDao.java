package com.es.phoneshop.dao.order;

import com.es.phoneshop.dao.Dao;
import com.es.phoneshop.model.order.Order;

import java.util.Optional;

public interface OrderDao extends Dao<Order> {

    Optional<Order> getOrderBySecureId(String secureId);

    void placeOrder(Order order);
}
