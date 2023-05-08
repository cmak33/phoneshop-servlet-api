package com.es.phoneshop.web;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.sorting.SortField;
import com.es.phoneshop.model.product.sorting.SortOrder;
import com.es.phoneshop.service.CustomProductService;
import com.es.phoneshop.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.EnumUtils;

import java.io.IOException;
import java.util.List;

public class ProductSortServlet extends HttpServlet {

    private ProductService productService;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productService = CustomProductService.getInstance();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String description = request.getParameter("query");
        String field = request.getParameter("field");
        String order = request.getParameter("order");
        List<Long> productsId = receiveProducts(description, field, order)
                .stream()
                .map(Product::getId)
                .toList();
        String json = objectMapper.writeValueAsString(productsId);
        response.getWriter().write(json);
    }

    private List<Product> receiveProducts(String description, String field, String order) {
        SortField sortField = EnumUtils.getEnumIgnoreCase(SortField.class, field);
        SortOrder sortOrder = EnumUtils.getEnumIgnoreCase(SortOrder.class, order);
        if (sortField != null && sortOrder != null) {
            return productService.findProductsByDescriptionWithOrdering(description, sortField, sortOrder);
        } else {
            return productService.findProductsByDescription(description);
        }
    }
}
