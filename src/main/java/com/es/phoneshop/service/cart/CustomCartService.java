package com.es.phoneshop.service.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.attributesHolder.AttributesHolder;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.CartProduct;
import com.es.phoneshop.service.product.CustomProductService;
import com.es.phoneshop.service.product.ProductService;

import java.util.List;
import java.util.Optional;

public class CustomCartService implements CartService {

    private static final String CART_ATTRIBUTE_NAME = String.format("%s.cart", CustomCartService.class.getName());
    private static volatile CustomCartService instance;
    private ProductService productService;

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
        return (Cart) attributesHolder.getAttribute(CART_ATTRIBUTE_NAME);
    }

    @Override
    public List<CartProduct> getCartProducts(AttributesHolder attributesHolder) {
        return getCart(attributesHolder).getCartItems()
                .stream()
                .map(item -> new CartProduct(productService.getProduct(item.getProductId()), item.getQuantity()))
                .toList();
    }

    @Override
    public void setCart(AttributesHolder attributesHolder, Cart cart) {
        attributesHolder.setAttribute(CART_ATTRIBUTE_NAME, cart);
    }

    @Override
    public void addItem(AttributesHolder attributesHolder, Long productId, int quantity) throws OutOfStockException {
        Cart cart = getCart(attributesHolder);
        synchronized (attributesHolder.getSynchronizationObject()) {
            Optional<CartItem> oldCartItem = findCartItemById(cart, productId);
            if (oldCartItem.isPresent()) {
                quantity += oldCartItem.get().getQuantity();
            }
            checkIfQuantityIsInStockBounds(productId, quantity);
            if (oldCartItem.isPresent()) {
                oldCartItem.get().setQuantity(quantity);
            } else {
                cart.addItem(new CartItem(productId, quantity));
            }
        }
    }

    private Optional<CartItem> findCartItemById(Cart cart, Long productId) {
        return cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();
    }

    private void checkIfQuantityIsInStockBounds(Long productId, int quantity) throws OutOfStockException {
        int stock = productService.getProduct(productId).getStock();
        if (quantity > stock) {
            throw new OutOfStockException(quantity, stock);
        }
    }
}
