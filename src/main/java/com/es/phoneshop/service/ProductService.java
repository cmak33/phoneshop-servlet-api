package com.es.phoneshop.service;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.sorting.SortField;
import com.es.phoneshop.model.product.sorting.SortOrder;

import java.util.List;

public interface ProductService {

    Product getProduct(Long id);

    List<Product> findProducts();

    List<Product> findSortedProducts(SortField sortField, SortOrder sortOrder);

    List<Product> findProductsByDescription(String description);

    List<Product> findProductsByDescriptionWithOrdering(String description, SortField sortField, SortOrder sortOrder);

    void save(Product product);

    void delete(Long id);
}
