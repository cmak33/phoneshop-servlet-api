package com.es.phoneshop.service.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotInCartException;
import com.es.phoneshop.model.attributesHolder.AttributesHolder;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.CartProduct;
import com.es.phoneshop.service.product.CustomProductService;
import com.es.phoneshop.service.product.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class CustomCartService implements CartService {

    public static final String CART_ATTRIBUTE_NAME = String.format("%s.cart", CustomCartService.class.getName());
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
        return getProductsFromCart(getCart(attributesHolder));
    }

    private List<CartProduct> getProductsFromCart(Cart cart) {
        return cart.getCartItems()
                .stream()
                .map(item -> new CartProduct(productService.getProduct(item.getProductId()), item.getQuantity()))
                .toList();
    }

    @Override
    public void updateItem(AttributesHolder attributesHolder, Long id, int newQuantity) throws OutOfStockException, ProductNotInCartException {
        Optional<CartItem> item = findCartItemById(getCart(attributesHolder), id);
        if (item.isPresent()) {
            checkIfQuantityIsInStockBounds(id, newQuantity);
            item.get().setQuantity(newQuantity);
            recalculateCartData(getCart(attributesHolder));
        } else {
            throw new ProductNotInCartException(id);
        }
    }

    @Override
    public void deleteItem(AttributesHolder attributesHolder, Long id) throws ProductNotInCartException {
        boolean wasRemoved;
        synchronized (attributesHolder.getSynchronizationObject()) {
            wasRemoved = getCart(attributesHolder).getCartItems()
                    .removeIf(product -> product.getProductId().equals(id));
        }
        if (!wasRemoved) {
            throw new ProductNotInCartException(id);
        } else {
            recalculateCartData(getCart(attributesHolder));
        }
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
            recalculateCartData(cart);
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

    private void recalculateCartData(Cart cart) {
        cart.setTotalQuantity(calculateTotalQuantity(cart));
        cart.setTotalCost(calculateTotalCost(cart));
    }

    private int calculateTotalQuantity(Cart cart) {
        return cart.getCartItems()
                .stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    private BigDecimal calculateTotalCost(Cart cart) {
        return getProductsFromCart(cart).stream()
                .map(item -> item.product()
                        .getPrice()
                        .multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
    }
}
