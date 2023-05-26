package com.es.phoneshop.dao.product;

import com.es.phoneshop.dao.Dao;
import com.es.phoneshop.model.product.Product;

import java.util.Comparator;
import java.util.List;

public interface ProductDao extends Dao<Product> {

    List<Product> findProducts();

    List<Product> findSortedProducts(Comparator<Product> comparator);

    List<Product> findProductsByDescription(String description);

    List<Product> findProductsByDescriptionWithOrdering(String description, Comparator<Product> comparator);

    void delete(Long id);
}
