package com.es.phoneshop.service;

import com.es.phoneshop.model.product.Product;

import java.util.List;

public interface ProductService {

    Product getProduct(Long id);

    List<Product> findProducts();

    List<Product> findProductsByDescription(String description);

    void save(Product product);

    void delete(Long id);
}
