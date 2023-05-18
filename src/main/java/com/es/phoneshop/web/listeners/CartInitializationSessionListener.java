package com.es.phoneshop.web.listeners;

import com.es.phoneshop.model.attributesHolder.HttpSessionAttributesHolder;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.cart.CustomCartService;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

public class CartInitializationSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        HttpSessionListener.super.sessionCreated(sessionEvent);
        String initializeCartParameter = sessionEvent.getSession()
                .getServletContext()
                .getInitParameter("initializeCart");
        boolean shouldInitialize = Boolean.parseBoolean(initializeCartParameter);
        if (shouldInitialize) {
            initializeCart(sessionEvent.getSession());
        }
    }

    private void initializeCart(HttpSession httpSession) {
        HttpSessionAttributesHolder attributesHolder = new HttpSessionAttributesHolder(httpSession);
        CustomCartService.getInstance().setCart(attributesHolder, new Cart());
    }
}
