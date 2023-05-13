package com.es.phoneshop.service.product.recentlyViewedProducts;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.attributesHolder.AttributesHolder;

import java.util.List;

public interface RecentlyViewedProductService {

    List<Product> getRecentlyViewedProducts(AttributesHolder attributesHolder);

    void addRecentlyViewedProduct(AttributesHolder attributesHolder, Long productId);
}
