package com.es.phoneshop.dao;

import com.es.phoneshop.dao.order.CustomOrderDao;
import com.es.phoneshop.model.order.Order;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class CustomOrderDaoTest {

    @Mock
    private Order order;
    private final CustomOrderDao orderDao = CustomOrderDao.getInstance();


    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        orderDao.setEntityList(new ArrayList<>() {{
            add(order);
        }});
    }

    @Test
    public void givenInvalidSecureId_whenGetOrderBySecureId_thenReturnOptionalEmpty() {
        String secureId = "invalid id";

        Optional<Order> actual = orderDao.getOrderBySecureId(secureId);

        assertTrue(actual.isEmpty());
    }

    @Test
    public void givenValidSecureId_whenGetOrderBySecureId_thenReturnOrder() {
        String secureId = "valid id";
        when(order.getSecureId()).thenReturn(secureId);

        Optional<Order> actual = orderDao.getOrderBySecureId(secureId);

        assertTrue(actual.isPresent());
        assertEquals(order, actual.get());
    }
}
