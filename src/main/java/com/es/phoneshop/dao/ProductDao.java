package com.es.phoneshop.dao;

import com.es.phoneshop.model.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    Optional<Product> getProduct(Long id);

    List<Product> findProducts();

    void save(Product product);

    void delete(Long id);
}
