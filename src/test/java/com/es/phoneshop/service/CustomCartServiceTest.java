package com.es.phoneshop.service;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.attributesHolder.AttributesHolder;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.cart.CustomCartService;
import com.es.phoneshop.service.product.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomCartServiceTest {

    @Mock
    private AttributesHolder attributesHolder;
    @Mock
    private ProductService productService;
    @Mock
    private CartItem cartItem;
    @Mock
    private Cart cart;
    @InjectMocks
    @Spy
    private CustomCartService cartService = CustomCartService.getInstance();

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockCartItems();
    }

    private void mockCartItems() {
        when(cartItem.getProductId()).thenReturn(1L);
        when(cartItem.getQuantity()).thenReturn(10);
        List<CartItem> items = new ArrayList<>() {{
            add(cartItem);
        }};
        when(cart.getCartItems()).thenReturn(items);

    }

    @Test
    public void givenValidCartAttribute_whenGetCart_thenReturnCart() {
        when(attributesHolder.getAttribute(any())).thenReturn(cart);

        Cart actualCart = cartService.getCart(attributesHolder);

        verify(attributesHolder).getAttribute(any());
        assertEquals(cart, actualCart);
    }

    @Test
    public void givenProductIsAlreadyInCart_whenAddItem_thenAddQuantity() throws OutOfStockException {
        int quantity = 10;
        int stock = 100;
        int expectedQuantity = quantity + cartItem.getQuantity();
        Product product = new Product.ProductBuilder()
                .setStock(stock)
                .setPrice(BigDecimal.valueOf(1))
                .build();
        when(productService.getProduct(cartItem.getProductId())).thenReturn(product);
        when(attributesHolder.getAttribute(any())).thenReturn(cart);
        when(attributesHolder.getSynchronizationObject()).thenReturn(new Object());

        cartService.addItem(attributesHolder, cartItem.getProductId(), quantity);

        verify(cartItem).setQuantity(expectedQuantity);
    }

    @Test(expected = OutOfStockException.class)
    public void givenOutOfStockQuantity_whenAddItem_thenThrowOutOfStockException() throws OutOfStockException {
        int quantity = 11;
        int stock = 10;
        Product product = new Product.ProductBuilder()
                .setStock(stock)
                .build();
        when(productService.getProduct(cartItem.getProductId())).thenReturn(product);
        when(attributesHolder.getAttribute(any())).thenReturn(cart);
        when(attributesHolder.getSynchronizationObject()).thenReturn(new Object());

        cartService.addItem(attributesHolder, cartItem.getProductId(), quantity);
    }

    @Test
    public void givenValidQuantity_whenAddItem_thenCreateNewItem() throws OutOfStockException {
        long id = 2L;
        int quantity = 1;
        int stock = 2;
        CartItem expectedItem = new CartItem(id, quantity);
        Product product = new Product.ProductBuilder()
                .setStock(stock)
                .setPrice(BigDecimal.valueOf(1))
                .build();
        when(productService.getProduct(id)).thenReturn(product);
        when(productService.getProduct(cartItem.getProductId())).thenReturn(product);
        when(attributesHolder.getSynchronizationObject()).thenReturn(new Object());
        when(attributesHolder.getAttribute(any())).thenReturn(cart);

        cartService.addItem(attributesHolder, id, quantity);

        verify(cart).addItem(expectedItem);
    }

    @Test
    public void givenCartObject_whenSetCart_thenSetCartToSessionAttribute() {
        cartService.setCart(attributesHolder, cart);

        verify(attributesHolder).setAttribute(any(), eq(cart));
    }
}
