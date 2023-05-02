package com.es.phoneshop.service;

import com.es.phoneshop.dao.CustomProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.dao.ProductDao;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class CustomProductService implements ProductService {

    private static CustomProductService instance;
    private ProductDao productDao;

    private CustomProductService() {
        productDao = CustomProductDao.getInstance();
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
        Optional<Product> product = productDao.getProduct(id);
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new NoSuchElementException(String.format("Product with id %d was not found", id));
        }
    }

    @Override
    public List<Product> findProducts() {
        return productDao.findProducts();
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
