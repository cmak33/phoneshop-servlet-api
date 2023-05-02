package com.es.phoneshop.service;

import com.es.phoneshop.dao.CustomProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.dao.ProductDao;

import java.util.List;

public class ProductService {
    private static ProductService instance;
    private final ProductDao productDao;

    private ProductService() {
        productDao = CustomProductDao.getInstance();
    }

    public static ProductService getInstance() {
        if (instance == null) {
            synchronized (ProductService.class) {
                if (instance == null) {
                    instance = new ProductService();
                }
            }
        }
        return instance;
    }

    public List<Product> findProducts() {
        return productDao.findProducts();
    }
}
