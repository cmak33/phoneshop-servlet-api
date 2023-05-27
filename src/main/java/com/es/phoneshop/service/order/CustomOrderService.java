package com.es.phoneshop.service.order;

import com.es.phoneshop.dao.order.CustomOrderDao;
import com.es.phoneshop.dao.order.OrderDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.ProductAndQuantity;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.CustomCartService;

import java.math.BigDecimal;
import java.util.List;

public class CustomOrderService implements OrderService {

    private static volatile CustomOrderService instance;
    private static final BigDecimal DEFAULT_DELIVERY_COST = BigDecimal.TEN;
    private final OrderDao orderDao;
    private final CartService cartService;

    private CustomOrderService() {
        orderDao = CustomOrderDao.getInstance();
        cartService = CustomCartService.getInstance();
    }

    public static CustomOrderService getInstance() {
        if (instance == null) {
            synchronized (CustomOrderService.class) {
                if (instance == null) {
                    instance = new CustomOrderService();
                }
            }
        }
        return instance;
    }

    @Override
    public Order createOrder(Cart cart) {
        Order order = new Order();
        order.getCartItems().addAll(cart.getCartItems().stream().map(CartItem::clone).toList());
        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryCost(DEFAULT_DELIVERY_COST);
        order.setTotalQuantity(cart.getTotalQuantity());
        order.setTotalCost(order.getSubtotal().add(order.getDeliveryCost()));
        return order;
    }

    @Override
    public Order getOrderBySecureId(String id) {
        return orderDao.getOrderBySecureId(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    public List<ProductAndQuantity> getOrderProducts(Order order) {
        return cartService.getCartProducts(order);
    }

    @Override
    public void placeOrder(Order order) {
        order.generateSecureId();
        orderDao.save(order);
    }
}
