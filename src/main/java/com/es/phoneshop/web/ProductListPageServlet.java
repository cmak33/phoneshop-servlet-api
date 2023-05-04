package com.es.phoneshop.web;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.sorting.SortField;
import com.es.phoneshop.model.product.sorting.SortOrder;
import com.es.phoneshop.service.CustomProductService;
import com.es.phoneshop.service.ProductService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.EnumUtils;

import java.io.IOException;
import java.util.List;

public class ProductListPageServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productService = CustomProductService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String description = request.getParameter("query");
        String field = request.getParameter("field");
        String order = request.getParameter("order");
        request.setAttribute("products", receiveProducts(description, field, order));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
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

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
