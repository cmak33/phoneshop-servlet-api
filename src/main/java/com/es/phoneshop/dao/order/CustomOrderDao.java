package com.es.phoneshop.dao.order;

import com.es.phoneshop.dao.genericDao.GenericDao;
import com.es.phoneshop.model.order.Order;

public class CustomOrderDao extends GenericDao<String, Order> implements OrderDao {

    private static volatile CustomOrderDao instance;

    private CustomOrderDao() {
        super();
    }

    public static CustomOrderDao getInstance() {
        if (instance == null) {
            synchronized (CustomOrderDao.class) {
                if (instance == null) {
                    instance = new CustomOrderDao();
                }
            }
        }
        return instance;
    }

    @Override
    public void placeOrder(Order order) {
        save(order);
    }
}
