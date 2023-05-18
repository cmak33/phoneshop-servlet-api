package com.es.phoneshop.web.listeners;

import com.es.phoneshop.model.attributesHolder.HttpSessionAttributesHolder;
import com.es.phoneshop.service.product.recentlyViewedProducts.CustomRecentlyViewedProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.util.ArrayList;

public class RecentlyViewedProductsInitializationSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        HttpSessionListener.super.sessionCreated(sessionEvent);
        String initializeViewedProductsParameter = sessionEvent.getSession()
                .getServletContext()
                .getInitParameter("initializeViewedProducts");
        boolean shouldInitialize = Boolean.parseBoolean(initializeViewedProductsParameter);
        if (shouldInitialize) {
            initializeViewedProducts(sessionEvent.getSession());
        }
    }

    private void initializeViewedProducts(HttpSession httpSession) {
        HttpSessionAttributesHolder attributesHolder = new HttpSessionAttributesHolder(httpSession);
        CustomRecentlyViewedProductService.getInstance().setRecentlyViewedProducts(attributesHolder, new ArrayList<>());
    }
}
