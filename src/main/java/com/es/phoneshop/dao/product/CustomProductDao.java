package com.es.phoneshop.dao.product;

import com.es.phoneshop.dao.genericDao.GenericDao;
import com.es.phoneshop.exception.notFoundException.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDescriptionMatch;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class CustomProductDao extends GenericDao<Long, Product> implements ProductDao {

    private static volatile CustomProductDao instance;

    private CustomProductDao() {
        super();
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

    @Override
    public List<Product> findProducts() {
        getReadLock().lock();
        try {
            return getEntityList().stream()
                    .filter(product -> product.getPrice() != null && product.getStock() > 0)
                    .toList();
        } finally {
            getReadLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts(Predicate<Product> predicate) {
        return findProducts().stream()
                .filter(predicate)
                .toList();
    }

    @Override
    public List<Product> findSortedProducts(Comparator<Product> comparator) {
        return findProducts().stream()
                .sorted(comparator)
                .toList();
    }

    @Override
    public List<Product> findProductsByDescription(String description) {
        getReadLock().lock();
        try {
            List<String> descriptionWords = Arrays.asList(description.toLowerCase().split(" "));
            Comparator<ProductDescriptionMatch> descriptionMatchComparator = Comparator.comparing(ProductDescriptionMatch::matchingWordsCount)
                    .reversed()
                    .thenComparing(ProductDescriptionMatch::productDescriptionWordsCount);
            return findProducts().stream()
                    .map(product -> createProductDescriptionMatch(product, descriptionWords))
                    .filter(productDescriptionMatch -> productDescriptionMatch.matchingWordsCount() > 0)
                    .sorted(descriptionMatchComparator)
                    .map(ProductDescriptionMatch::product)
                    .toList();
        } finally {
            getReadLock().unlock();
        }
    }

    private ProductDescriptionMatch createProductDescriptionMatch(Product product, List<String> descriptionWords) {
        List<String> productDescriptionWords = Arrays.asList(product.getDescription().toLowerCase().split(" "));
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
    public void updateStock(Product product, int newStock) {
        product.setStock(newStock);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id was null");
        }
        getWriteLock().lock();
        try {
            if (!getEntityList().removeIf(product -> id.equals(product.getId()))) {
                throw new ProductNotFoundException(id);
            }
        } finally {
            getWriteLock().unlock();
        }
    }
}
