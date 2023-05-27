package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.Product;

public record ProductAndQuantity(Product product, int quantity) {
}
