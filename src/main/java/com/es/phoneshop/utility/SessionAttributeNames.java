package com.es.phoneshop.utility;

import com.es.phoneshop.service.cart.CustomCartService;
import com.es.phoneshop.service.product.recentlyViewedProducts.CustomRecentlyViewedProductService;

public class SessionAttributeNames {

    public static final String CART_ATTRIBUTE_NAME = String.format("%s.cart", CustomCartService.class.getName());
    public static final String VIEWED_PRODUCTS_ATTRIBUTE_NAME = String.format("%s.viewed",
            CustomRecentlyViewedProductService.class.getName());
}
