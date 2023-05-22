package com.es.phoneshop.web.servlets;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.QuantityParseException;
import com.es.phoneshop.model.attributesHolder.AttributesHolder;
import com.es.phoneshop.model.attributesHolder.HttpSessionAttributesHolder;
import com.es.phoneshop.model.parser.QuantityParser;
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

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CustomCartService.getInstance();
        quantityParser = new QuantityParser();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AttributesHolder attributesHolder = new HttpSessionAttributesHolder(request.getSession());
        request.setAttribute("products", cartService.getCartProducts(attributesHolder));
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<Long, String> errors = new HashMap<>();
        String[] productsId = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");
        AttributesHolder attributesHolder = new HttpSessionAttributesHolder(request.getSession());
        for (int i = 0; i < productsId.length; i++) {
            Long id = Long.valueOf(productsId[i]);
            try {
                int quantity = quantityParser.parse(request.getLocale(), quantities[i]);
                updateProduct(attributesHolder, id, quantity);
            } catch (QuantityParseException | OutOfStockException exception) {
                errors.put(id, exception.getMessage());
            }
        }
        if (errors.isEmpty()) {
            response.sendRedirect(createSuccessUrl(request));
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }

    private void updateProduct(AttributesHolder attributesHolder, Long id, int quantity) throws OutOfStockException {
        cartService.updateItem(attributesHolder, id, quantity);
    }

    private String createSuccessUrl(HttpServletRequest request) {
        return String.format("%s/cart?message=Cart was updated successfully", request.getContextPath());
    }
}
