package com.es.phoneshop.service.product.recentlyViewedProducts;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.attributesHolder.AttributesHolder;
import com.es.phoneshop.service.product.CustomProductService;
import com.es.phoneshop.service.product.ProductService;

import java.util.ArrayList;
import java.util.List;

public class CustomRecentlyViewedProductService implements RecentlyViewedProductService {

    private static final int VIEWED_PRODUCTS_COUNT = 3;
    private static final String VIEWED_PRODUCTS_ATTRIBUTE_NAME = String.format("%s.viewed",
            CustomRecentlyViewedProductService.class.getName());
    private static volatile CustomRecentlyViewedProductService instance;
    private ProductService productService;

    private CustomRecentlyViewedProductService() {
        productService = CustomProductService.getInstance();
    }

    public static CustomRecentlyViewedProductService getInstance() {
        if (instance == null) {
            synchronized (CustomRecentlyViewedProductService.class) {
                if (instance == null) {
                    instance = new CustomRecentlyViewedProductService();
                }
            }
        }
        return instance;
    }

    @Override
    public List<Product> getRecentlyViewedProducts(AttributesHolder attributesHolder) {
        return getCurrentlyViewedProductsId(attributesHolder).stream()
                .map(productService::getProduct)
                .toList();
    }

    @Override
    public void addRecentlyViewedProduct(AttributesHolder attributesHolder, Long productId) {
        List<Long> productsId = getCurrentlyViewedProductsId(attributesHolder);
        synchronized (productsId) {
            if (productsId.contains(productId)) {
                productsId.remove(productId);
            } else if (productsId.size() == VIEWED_PRODUCTS_COUNT) {
                productsId.remove(productsId.size() - 1);
            }
            productsId.add(0, productId);
        }
    }

    private List<Long> getCurrentlyViewedProductsId(AttributesHolder attributesHolder) {
        List<Long> productsId = (List<Long>) attributesHolder.getAttribute(VIEWED_PRODUCTS_ATTRIBUTE_NAME);
        if (productsId == null) {
            productsId = new ArrayList<>();
            attributesHolder.setAttribute(VIEWED_PRODUCTS_ATTRIBUTE_NAME, productsId);
        }
        return productsId;
    }
}
