package com.es.phoneshop.service.product;

import com.es.phoneshop.configuration.ProductComparatorsConfiguration;
import com.es.phoneshop.dao.product.CustomProductDao;
import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.exception.NegativeStockException;
import com.es.phoneshop.exception.notFoundException.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.sorting.SortField;
import com.es.phoneshop.model.product.sorting.SortOrder;

import java.util.Comparator;
import java.util.List;

public class CustomProductService implements ProductService {

    private static volatile CustomProductService instance;
    private ProductDao productDao;
    private ProductComparatorsConfiguration productComparatorsConfiguration;

    private CustomProductService() {
        productDao = CustomProductDao.getInstance();
        productComparatorsConfiguration = ProductComparatorsConfiguration.getInstance();
    }

    public static CustomProductService getInstance() {
        if (instance == null) {
            synchronized (CustomProductService.class) {
                if (instance == null) {
                    instance = new CustomProductService();
                }
            }
        }
        return instance;
    }

    @Override
    public Product getProduct(Long id) {
        return productDao.getEntity(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public List<Product> findProducts() {
        return productDao.findProducts();
    }

    @Override
    public List<Product> findSortedProducts(SortField sortField, SortOrder sortOrder) {
        Comparator<Product> productComparator = productComparatorsConfiguration.getComparatorByFieldAndOrder(sortField, sortOrder);
        return productDao.findSortedProducts(productComparator);
    }

    @Override
    public List<Product> findProductsByDescription(String description) {
        return productDao.findProductsByDescription(description);
    }

    @Override
    public List<Product> findProductsByDescriptionWithOrdering(String description, SortField sortField, SortOrder sortOrder) {
        Comparator<Product> productComparator = productComparatorsConfiguration.getComparatorByFieldAndOrder(sortField, sortOrder);
        return productDao.findProductsByDescriptionWithOrdering(description, productComparator);
    }

    @Override
    public void changeStock(Long id, int changeInStock) {
        Product product = getProduct(id);
        synchronized (product) {
            int newStock = product.getStock() + changeInStock;
            if (newStock < 0) {
                throw new NegativeStockException();
            }
            productDao.updateStock(product, newStock);
        }
    }

    @Override
    public void save(Product product) {
        productDao.save(product);
    }

    @Override
    public void delete(Long id) {
        productDao.delete(id);
    }
}
