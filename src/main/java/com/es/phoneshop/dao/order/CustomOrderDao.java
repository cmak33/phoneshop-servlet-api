package com.es.phoneshop.dao.order;

import com.es.phoneshop.dao.genericDao.GenericDao;
import com.es.phoneshop.model.order.Order;

import java.util.Optional;

public class CustomOrderDao extends GenericDao<Order> implements OrderDao {

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
    public Optional<Order> getOrderBySecureId(String secureId) {
        return getEntityByField(secureId, Order::getSecureId);
    }
}
