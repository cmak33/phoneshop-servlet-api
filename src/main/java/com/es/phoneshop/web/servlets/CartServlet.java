package com.es.phoneshop.web.servlets;

import com.es.phoneshop.exception.CustomParseException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.attributesHolder.AttributesHolder;
import com.es.phoneshop.model.attributesHolder.HttpSessionAttributesHolder;
import com.es.phoneshop.model.parser.QuantityParser;
import com.es.phoneshop.validator.validator.QuantityValidator;
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

public class CartServlet extends HttpServlet {

    private CartService cartService;
    private QuantityParser quantityParser;
    private QuantityValidator quantityValidator;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CustomCartService.getInstance();
        quantityParser = QuantityParser.getInstance();
        quantityValidator = QuantityValidator.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AttributesHolder attributesHolder = new HttpSessionAttributesHolder(request.getSession());
        request.setAttribute("products", cartService.getCartProducts(attributesHolder));
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productsId = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");
        if (productsId != null && quantities != null) {
            Map<Long, String> errors = new HashMap<>();
            AttributesHolder attributesHolder = new HttpSessionAttributesHolder(request.getSession());
            for (int i = 0; i < productsId.length; i++) {
                Long id = Long.valueOf(productsId[i]);
                if (quantityValidator.validate(quantities[i], request.getLocale(), errors, id)) {
                    try {
                        int quantity = quantityParser.parse(quantities[i], request.getLocale());
                        cartService.updateCartItem(attributesHolder, id, quantity);
                    } catch (CustomParseException | OutOfStockException exception) {
                        errors.put(id, exception.getMessage());
                    }
                }
            }
            if (errors.isEmpty()) {
                response.sendRedirect(createSuccessUrl(request));
            } else {
                request.setAttribute("errors", errors);
                doGet(request, response);
            }
        } else {
            response.sendRedirect(createEmptyCartErrorUrl(request));
        }
    }

    private String createSuccessUrl(HttpServletRequest request) {
        return String.format("%s/cart?message=Cart was updated successfully", request.getContextPath());
    }

    private String createEmptyCartErrorUrl(HttpServletRequest request) {
        return String.format("%s/cart?errorMessage=There were no products to update", request.getContextPath());
    }
}
