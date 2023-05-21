package com.es.phoneshop.web.servlets;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.attributesHolder.HttpSessionAttributesHolder;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.CustomCartService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class CartAddItemServlet extends HttpServlet {

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
            String redirectionUrl = request.getParameter("redirectionPath");
            redirectionUrl += createSeparatorCharacter(redirectionUrl);
            int quantity;
            try {
                quantity = parseQuantity(request.getLocale(), request.getParameter("quantity"));
            } catch (ParseException parseException) {
                response.sendRedirect(createErrorRedirectUrl(redirectionUrl, id, "Quantity was not a number"));
                return;
            }
            if (quantity <= 0) {
                response.sendRedirect(createErrorRedirectUrl(redirectionUrl, id, "Quantity should be bigger than zero"));
                return;
            }
            try {
                addItemToCart(request, id, quantity);
            } catch (OutOfStockException exception) {
                response.sendRedirect(createErrorRedirectUrl(redirectionUrl, id, exception.getMessage()));
                return;
            }
            response.sendRedirect(createSuccessRedirectUrl(redirectionUrl, id, quantity));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private int parseQuantity(Locale locale, String quantity) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        return numberFormat.parse(quantity).intValue();
    }

    private void addItemToCart(HttpServletRequest request, Long id, int quantity) throws OutOfStockException {
        HttpSessionAttributesHolder httpSessionAttributesHolder = new HttpSessionAttributesHolder(request.getSession());
        cartService.addItem(httpSessionAttributesHolder, id, quantity);
    }

    private String createErrorRedirectUrl(String redirectionUrl, Long productId, String message) {
        String query = String.format("errorMessage=%s&id=%d", message, productId);
        return redirectionUrl.concat(query);
    }

    private String createSuccessRedirectUrl(String redirectionUrl, Long productId, int quantity) {
        String successMessage = "Product was successfully added to cart";
        String query = String.format("message=%s&id=%d&quantity=%d", successMessage, productId, quantity);
        return redirectionUrl.concat(query);
    }

    private char createSeparatorCharacter(String url) {
        return url.contains("?") ? '&' : '?';
    }
}
