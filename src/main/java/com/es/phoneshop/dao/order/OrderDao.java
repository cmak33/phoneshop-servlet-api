package com.es.phoneshop.dao.order;

import com.es.phoneshop.dao.Dao;
import com.es.phoneshop.model.order.Order;

public interface OrderDao extends Dao<Long, Order> {

    void placeOrder(Order order);
}
