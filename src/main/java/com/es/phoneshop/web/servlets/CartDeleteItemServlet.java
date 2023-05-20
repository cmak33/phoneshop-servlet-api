package com.es.phoneshop.web.servlets;

import com.es.phoneshop.exception.ProductNotInCartException;
import com.es.phoneshop.model.attributesHolder.AttributesHolder;
import com.es.phoneshop.model.attributesHolder.HttpSessionAttributesHolder;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.CustomCartService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CartDeleteItemServlet extends HttpServlet {

    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CustomCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getPathInfo() != null && request.getPathInfo().length() > 1) {
            long id = Long.parseLong(request.getPathInfo().substring(1));
            try {
                AttributesHolder attributesHolder = new HttpSessionAttributesHolder(request.getSession());
                cartService.deleteItem(attributesHolder, id);
            } catch (ProductNotInCartException productNotInCartException) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            response.sendRedirect(createSuccessUrl(request));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private String createSuccessUrl(HttpServletRequest request) {
        return String.format("%s/cart?message=Item was successfully deleted", request.getContextPath());
    }
}
