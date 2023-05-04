package com.es.phoneshop.dao;

import com.es.phoneshop.model.product.Product;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public interface ProductDao {

    Optional<Product> getProduct(Long id);

    List<Product> findProducts();

    List<Product> findProductsByDescription(String description);

    List<Product> findProductsByDescriptionWithOrdering(String description, Comparator<Product> comparator);

    void save(Product product);

    void delete(Long id);
}
