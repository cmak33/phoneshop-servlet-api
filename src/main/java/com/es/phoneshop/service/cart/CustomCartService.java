package com.es.phoneshop.service.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.service.product.CustomProductService;
import com.es.phoneshop.service.product.ProductService;

import java.util.Optional;

public class CustomCartService implements CartService {

    private static final String CART_ATTRIBUTE_NAME = String.format("%s.cart", CustomCartService.class.getName());
    private static volatile CustomCartService instance;
    private final ProductService productService;

    private CustomCartService() {
        productService = CustomProductService.getInstance();
    }

    public static CustomCartService getInstance() {
        if (instance == null) {
            synchronized (CustomCartService.class) {
                if (instance == null) {
                    instance = new CustomCartService();
                }
            }
        }
        return instance;
    }

    @Override
    public Cart getCart(AttributesHolder attributesHolder) {
        Cart cart = (Cart) attributesHolder.getAttribute(CART_ATTRIBUTE_NAME);
        if (cart == null) {
            cart = new Cart();
            attributesHolder.setAttribute(CART_ATTRIBUTE_NAME, cart);
        }
        return cart;
    }

    @Override
    public void addItem(Cart cart, Long productId, int quantity) throws OutOfStockException {
        synchronized (cart) {
            Optional<CartItem> oldCartItem = cart.getCartItems().stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst();
            if (oldCartItem.isPresent()) {
                quantity += oldCartItem.get().getQuantity();
            }
            int stock = productService.getProduct(productId).getStock();
            if (quantity > stock) {
                throw new OutOfStockException(quantity, stock);
            }
            if (oldCartItem.isPresent()) {
                oldCartItem.get().setQuantity(quantity);
            } else {
                cart.addItem(new CartItem(productId, quantity));
            }
        }
    }
}
