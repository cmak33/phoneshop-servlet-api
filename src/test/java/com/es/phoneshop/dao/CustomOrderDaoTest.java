package com.es.phoneshop.dao;

import com.es.phoneshop.dao.order.CustomOrderDao;
import com.es.phoneshop.model.order.Order;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomOrderDaoTest {

    @Mock
    private Order order;
    @Spy
    private final CustomOrderDao orderDao = CustomOrderDao.getInstance();

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        orderDao.setEntityList(new ArrayList<>() {{
            add(order);
        }});
    }

    @Test
    public void givenOrder_whenPlaceOrder_thenSaveOrder() {
        orderDao.placeOrder(order);

        verify(orderDao).save(order);
    }

    @Test
    public void givenOrder_whenSaveOrder_thenAddOrderToList() {
        Order newOrder = new Order();

        orderDao.save(newOrder);

        assertTrue(orderDao.getEntityList().contains(newOrder));
    }

    @Test
    public void givenNullId_whenGetEntity_thenReturnEmptyOptional() {
        assertTrue(orderDao.getEntity(null).isEmpty());
    }

    @Test
    public void givenExistingOrderId_whenGetEntity_thenReturnOrder() {
        Long id = 1L;
        when(order.getId()).thenReturn(id);
        Optional<Order> actualOrder = orderDao.getEntity(id);

        assertTrue(actualOrder.isPresent());
        assertEquals(order, actualOrder.get());
    }
}
