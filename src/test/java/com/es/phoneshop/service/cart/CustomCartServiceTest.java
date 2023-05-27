package com.es.phoneshop.service.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.exception.ProductNotInCartException;
import com.es.phoneshop.model.attributesHolder.AttributesHolder;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.product.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    @Mock
    private Product cartProduct;
    @InjectMocks
    private CustomCartService cartService = CustomCartService.getInstance();


    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockCartItems();
        mockCartProduct();
        when(attributesHolder.getSynchronizationObject()).thenReturn(new Object());
        when(attributesHolder.getAttribute(any())).thenReturn(cart);
        when(productService.getProduct(cartItem.getProductId())).thenReturn(cartProduct);
    }

    private void mockCartItems() {
        when(cartItem.getProductId()).thenReturn(1L);
        when(cartItem.getQuantity()).thenReturn(10);
        List<CartItem> items = new ArrayList<>() {{
            add(cartItem);
        }};
        when(cart.getCartItems()).thenReturn(items);
    }

    private void mockCartProduct() {
        when(cartProduct.getStock()).thenReturn(100);
        when(cartProduct.getId()).thenReturn(1L);
        when(cartProduct.getPrice()).thenReturn(BigDecimal.ONE);
    }


    @Test
    public void givenValidCartAttribute_whenGetCart_thenReturnCart() {
        Cart actualCart = cartService.getCart(attributesHolder);

        verify(attributesHolder).getAttribute(any());
        assertEquals(cart, actualCart);
    }

    @Test
    public void givenProductIsAlreadyInCart_whenAddItem_thenAddQuantity() throws OutOfStockException {
        int quantity = 1;
        int expectedQuantity = quantity + cartItem.getQuantity();

        cartService.addCartItem(attributesHolder, cartItem.getProductId(), quantity);

        verify(cartItem).setQuantity(expectedQuantity);
    }

    @Test(expected = OutOfStockException.class)
    public void givenOutOfStockQuantity_whenAddItem_thenThrowOutOfStockException() throws OutOfStockException {
        int quantity = cartProduct.getStock() + 1;

        cartService.addCartItem(attributesHolder, cartItem.getProductId(), quantity);
    }

    @Test
    public void givenValidQuantity_whenAddItem_thenCreateNewItem() throws OutOfStockException {
        long id = 2L;
        int quantity = 1;
        CartItem expectedItem = new CartItem(id, quantity);
        when(productService.getProduct(id)).thenReturn(cartProduct);

        cartService.addCartItem(attributesHolder, id, quantity);

        verify(cart).addItem(expectedItem);
    }

    @Test
    public void givenCartObject_whenSetCart_thenSetCartToSessionAttribute() {
        cartService.setCart(attributesHolder, cart);

        verify(attributesHolder).setAttribute(any(), eq(cart));
    }

    @Test
    public void givenExistingProductId_whenDeleteItem_thenDelete() {
        List<CartItem> expected = new ArrayList<>(cart.getCartItems());
        expected.remove(cartItem);

        cartService.deleteCartItem(attributesHolder, cartItem.getProductId());

        assertEquals(expected, cart.getCartItems());
    }

    @Test(expected = ProductNotInCartException.class)
    public void givenNonExistingProductId_whenDeleteItem_thenThrowProductNotInCartException() {
        cartService.deleteCartItem(attributesHolder, -1L);
    }

    @Test
    public void givenValidQuantity_whenUpdateItem_thenChangeItemQuantity() throws OutOfStockException {
        int quantity = cartProduct.getStock();
        Long id = cartItem.getProductId();

        cartService.updateCartItem(attributesHolder, id, quantity);

        verify(cartItem).setQuantity(quantity);
    }

    @Test(expected = OutOfStockException.class)
    public void givenOutOfStockQuantity_whenUpdateItem_thenThrowOutOfStockException() throws OutOfStockException {
        int quantity = cartProduct.getStock() + 1;

        cartService.updateCartItem(attributesHolder, cartItem.getProductId(), quantity);
    }

    @Test(expected = ProductNotInCartException.class)
    public void givenInvalidId_whenUpdateItem_thenThrowProductNotInCartException() throws OutOfStockException {
        Long id = -1L;
        int quantity = 1;
        when(productService.getProduct(id)).thenThrow(new ProductNotFoundException(id));

        cartService.updateCartItem(attributesHolder, id, quantity);
    }


}
