package com.es.phoneshop.web.servlets;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.attributesHolder.HttpSessionAttributesHolder;
import com.es.phoneshop.service.product.CustomProductService;
import com.es.phoneshop.service.product.ProductService;
import com.es.phoneshop.service.product.recentlyViewedProducts.CustomRecentlyViewedProductService;
import com.es.phoneshop.service.product.recentlyViewedProducts.RecentlyViewedProductService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class ProductDetailsPageServlet extends HttpServlet {

    private ProductService productService;
    private RecentlyViewedProductService recentlyViewedProductService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productService = CustomProductService.getInstance();
        recentlyViewedProductService = CustomRecentlyViewedProductService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getPathInfo() != null && request.getPathInfo().length() > 1) {
            long id = Long.parseLong(request.getPathInfo().substring(1));
            Product product = productService.getProduct(id);
            addProductToRecentlyViewed(request, id);
            request.setAttribute("product", product);
            request.setAttribute("recentlyViewedProducts", receiveRecentlyViewedProducts(request));
            request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void addProductToRecentlyViewed(HttpServletRequest request, long id) {
        HttpSessionAttributesHolder attributesHolder = new HttpSessionAttributesHolder(request.getSession());
        recentlyViewedProductService.addRecentlyViewedProduct(attributesHolder, id);
    }

    private List<Product> receiveRecentlyViewedProducts(HttpServletRequest request) {
        HttpSessionAttributesHolder attributesHolder = new HttpSessionAttributesHolder(request.getSession());
        return recentlyViewedProductService.getRecentlyViewedProducts(attributesHolder);
    }
}
