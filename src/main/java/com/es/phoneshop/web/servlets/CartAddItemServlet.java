package com.es.phoneshop.web.servlets;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.attributesHolder.HttpSessionAttributesHolder;
import com.es.phoneshop.model.parser.QuantityValidator;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.CustomCartService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CartAddItemServlet extends HttpServlet {

    private CartService cartService;
    private QuantityValidator quantityValidator;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CustomCartService.getInstance();
        quantityValidator = QuantityValidator.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getPathInfo() != null && request.getPathInfo().length() > 1) {
            long id = Long.parseLong(request.getPathInfo().substring(1));
            String redirectionUrl = request.getParameter("redirectionPath");
            redirectionUrl += createSeparatorCharacter(redirectionUrl);
            Map<Long, String> errors = new HashMap<>();
            Optional<Integer> quantity = quantityValidator
                    .tryParse(errors, id, request.getLocale(), request.getParameter("quantity"));
            if (quantity.isEmpty()) {
                response.sendRedirect(createErrorRedirectUrl(redirectionUrl, id, errors.get(id)));
                return;
            }
            try {
                addItemToCart(request, id, quantity.get());
            } catch (OutOfStockException exception) {
                response.sendRedirect(createErrorRedirectUrl(redirectionUrl, id, exception.getMessage()));
                return;
            }
            response.sendRedirect(createSuccessRedirectUrl(redirectionUrl, id, quantity.get()));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
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
