package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.Product;

public record CartProduct(Product product, int quantity) {
}
