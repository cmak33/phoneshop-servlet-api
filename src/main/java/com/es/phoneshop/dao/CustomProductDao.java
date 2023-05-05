package com.es.phoneshop.dao;

import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CustomProductDao implements ProductDao {

    private static CustomProductDao instance;
    private final Lock readLock;
    private final Lock writeLock;
    private List<Product> productList;

    private CustomProductDao() {
        productList = new ArrayList<>();
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }

    public static CustomProductDao getInstance() {
        if (instance == null) {
            synchronized (CustomProductDao.class) {
                if (instance == null) {
                    instance = new CustomProductDao();
                }
            }
        }
        return instance;
    }

    private record ProductDescriptionMatch(Product product, int productDescriptionWordsCount,
                                           int matchingWordsCount) {
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        if (id != null) {
            readLock.lock();
            try {
                return productList.stream()
                        .filter(product -> id.equals(product.getId()))
                        .findAny();
            } finally {
                readLock.unlock();
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Product> findProducts() {
        readLock.lock();
        try {
            return productList.stream()
                    .filter(product -> product.getPrice() != null && product.getStock() > 0)
                    .toList();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public List<Product> findProductsByDescription(String description) {
        if (description == null || description.isBlank()) {
            return findProducts();
        }
        readLock.lock();
        try {
            List<String> descriptionWords = Arrays.asList(description.split(" "));
            return findProducts().stream()
                    .map(product -> createProductDescriptionMatch(product, descriptionWords))
                    .filter(productDescriptionMatch -> productDescriptionMatch.matchingWordsCount > 0)
                    .sorted(Comparator.comparing(ProductDescriptionMatch::matchingWordsCount).reversed().thenComparing(ProductDescriptionMatch::productDescriptionWordsCount))
                    .map(productDescriptionMatch -> productDescriptionMatch.product)
                    .toList();
        } finally {
            readLock.unlock();
        }
    }

    private ProductDescriptionMatch createProductDescriptionMatch(Product product, List<String> descriptionWords) {
        List<String> productDescriptionWords = Arrays.asList(product.getDescription().split(" "));
        int matchingWordsCount = (int) descriptionWords.stream()
                .distinct()
                .filter(productDescriptionWords::contains)
                .count();
        return new ProductDescriptionMatch(product, productDescriptionWords.size(), matchingWordsCount);
    }

    @Override
    public List<Product> findProductsByDescriptionWithOrdering(String description, Comparator<Product> comparator) {
        return findProductsByDescription(description).stream()
                .sorted(comparator)
                .toList();
    }

    @Override
    public void save(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product was null");
        }
        writeLock.lock();
        try {
            Optional<Product> productWithSameId = getProduct(product.getId());
            if (productWithSameId.isPresent()) {
                int productWithSameIdIndex = productList.indexOf(productWithSameId.get());
                productList.set(productWithSameIdIndex, product);
            } else {
                productList.add(product);
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id was null");
        }
        writeLock.lock();
        try {
            if (!productList.removeIf(product -> id.equals(product.getId()))) {
                throw new ProductNotFoundException(id);
            }
        } finally {
            writeLock.unlock();
        }
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
