package com.es.phoneshop.service.order;

import com.es.phoneshop.dao.order.OrderDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.CartProduct;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.cart.CartService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomOrderServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private CartService cartService;
    @Mock
    private Order order;
    @InjectMocks
    private CustomOrderService customOrderService = CustomOrderService.getInstance();

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenValidCart_whenCreateOrder_thenReturnOrder() {
        int quantity = 100;
        BigDecimal cartTotalCost = BigDecimal.TEN;
        Cart cart = new Cart();
        cart.getCartItems().add(new CartItem(1L, quantity));
        cart.setTotalQuantity(quantity);
        cart.setTotalCost(cartTotalCost);

        Order order = customOrderService.createOrder(cart);

        assertEquals(cart.getCartItems(), order.getCartItems());
        assertEquals(cart.getTotalQuantity(), order.getTotalQuantity());
        assertEquals(cart.getTotalCost(), order.getSubtotal());
        assertEquals(cart.getTotalCost().add(order.getDeliveryCost()), order.getTotalCost());
    }

    @Test
    public void givenValidSecureId_whenGetOrderBySecureId_thenReturnOrder() {
        String secureId = "secure id";
        when(orderDao.getOrderBySecureId(secureId)).thenReturn(Optional.of(order));

        Order actual = customOrderService.getOrderBySecureId(secureId);

        assertEquals(order, actual);
    }

    @Test(expected = OrderNotFoundException.class)
    public void givenInvalidSecureId_whenGetOrderBySecureId_thenThrowOrderNotFoundException() {
        String invalidSecureId = "invalid id";
        when(orderDao.getOrderBySecureId(invalidSecureId)).thenReturn(Optional.empty());

        customOrderService.getOrderBySecureId(invalidSecureId);
    }

    @Test
    public void givenOrder_whenGetOrderProducts_thenReturnProducts() {
        List<CartProduct> productList = new ArrayList<>();
        productList.add(new CartProduct(new Product(), 100));
        when(cartService.getCartProducts(order)).thenReturn(productList);

        List<CartProduct> actual = customOrderService.getOrderProducts(order);

        assertEquals(productList, actual);
    }

    @Test
    public void givenOrder_whenPlaceOrder_thenSaveOrder() {
        customOrderService.placeOrder(order);

        verify(orderDao).placeOrder(order);
    }
}
