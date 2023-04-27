package com.es.phoneshop.model.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayListProductDao implements ProductDao {
    private final List<Product> productList;
    private long currentId = 1L;

    public ArrayListProductDao() {
        productList = Collections.synchronizedList(new ArrayList<>());
    }

    public ArrayListProductDao(List<Product> products) {
        this();
        products.forEach(this::save);
    }

    @Override
    public Product getProduct(Long id) {
        return productList.stream()
                .filter(product -> product.getId().equals(id))
                .findAny()
                .orElse(null);
    }

    @Override
    public List<Product> findProducts() {
        List<Product> unmodifiableProductList = productList.stream()
                .filter(product -> product.getPrice() != null && product.getStock() > 0)
                .toList();
        return new ArrayList<>(unmodifiableProductList);
    }

    @Override
    public synchronized void save(Product product) {
        if (product.getId() != null) {
            delete(product.getId());
        } else {
            product.setId(currentId);
            currentId++;
        }
        productList.add(product);
    }

    @Override
    public void delete(Long id) {
        Product productToDelete = getProduct(id);
        productList.remove(productToDelete);
    }
}
