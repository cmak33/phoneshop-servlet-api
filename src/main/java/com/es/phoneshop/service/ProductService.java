package com.es.phoneshop.service;

import com.es.phoneshop.dao.CustomProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.dao.ProductDao;

import java.util.List;

public class ProductService {
    private final ProductDao productDao;

    private ProductService() {
        productDao = CustomProductDao.getInstance();
    }

    private static final class InstanceHolder {
        private static final ProductService instance = new ProductService();
    }

    public static ProductService getInstance() {
        return ProductService.InstanceHolder.instance;
    }

    public List<Product> findProducts() {
        return productDao.findProducts();
    }
}
