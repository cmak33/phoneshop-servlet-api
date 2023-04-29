package com.es.phoneshop.model.product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CustomProductDao implements ProductDao {
    private final List<Product> productList;
    private final Lock readLock;
    private final Lock writeLock;
    private long currentId = 1L;

    public CustomProductDao() {
        productList = new ArrayList<>();
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }

    public CustomProductDao(List<Product> products) {
        this();
        products.forEach(this::save);
    }

    @Override
    public Product getProduct(Long id) {
        Product result;
        readLock.lock();
        try {
            result = productList.stream()
                    .filter(product -> product.getId().equals(id))
                    .findAny()
                    .orElse(null);
        } finally {
            readLock.unlock();
        }
        return result;
    }

    @Override
    public List<Product> findProducts() {
        List<Product> unmodifiableProductList;
        readLock.lock();
        try {
            unmodifiableProductList = productList.stream()
                    .filter(product -> product.getPrice() != null && product.getStock() > 0)
                    .toList();
        } finally {
            readLock.unlock();
        }
        return new ArrayList<>(unmodifiableProductList);
    }

    @Override
    public void save(Product product) {
        writeLock.lock();
        try {
            if (product.getId() != null) {
                delete(product.getId());
            } else {
                product.setId(currentId);
                currentId++;
            }
            productList.add(product);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void delete(Long id) {
        Product productToDelete = getProduct(id);
        writeLock.lock();
        try {
            productList.remove(productToDelete);
        } finally {
            writeLock.unlock();
        }
    }

    public void clear() {
        writeLock.lock();
        try {
            productList.clear();
        } finally {
            writeLock.unlock();
        }
    }
}
