package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.CustomCartService;
import com.es.phoneshop.model.attributesHolder.HttpSessionAttributesHolder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CartServlet extends HttpServlet {

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
            int quantity;
            try {
                quantity = Integer.parseInt(request.getParameter("quantity"));
            } catch (NumberFormatException numberFormatException) {
                response.sendRedirect(createErrorRedirectUrl(request, id, "Quantity was not a number"));
                return;
            }
            if (quantity <= 0) {
                response.sendRedirect(createErrorRedirectUrl(request, id, "Quantity should be bigger than zero"));
                return;
            }
            try {
                addItemToCart(request, id, quantity);
            } catch (OutOfStockException exception) {
                response.sendRedirect(createErrorRedirectUrl(request, id, exception.getMessage()));
                return;
            }
            response.sendRedirect(createSuccessRedirectUrl(request, id, quantity));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void addItemToCart(HttpServletRequest request, Long id, int quantity) throws OutOfStockException {
        HttpSessionAttributesHolder httpSessionAttributesHolder = new HttpSessionAttributesHolder(request.getSession());
        Cart cart = cartService.getCart(httpSessionAttributesHolder);
        cartService.addItem(cart, id, quantity);
    }

    private String createErrorRedirectUrl(HttpServletRequest request, Long productId, String message) {
        return String.format("%s/products/%d?errorMessage=%s", request.getContextPath(), productId, message);
    }

    private String createSuccessRedirectUrl(HttpServletRequest request, Long productId, int quantity) {
        String successMessage = "Product was successfully added to cart";
        return String.format("%s/products/%d?message=%s&quantity=%d", request.getContextPath(), productId, successMessage, quantity);
    }
}
