package com.es.phoneshop.web;

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
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;

public class ProductListPageServlet extends HttpServlet {

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
        String description = request.getParameter("query");
        request.setAttribute("products", receiveProducts(description));
        request.setAttribute("recentlyViewedProducts", receiveRecentlyViewedProducts(request));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    private List<Product> receiveProducts(String description) {
        if (StringUtils.isBlank(description)) {
            return productService.findProducts();
        } else {
            return productService.findProductsByDescription(description);
        }
    }

    private List<Product> receiveRecentlyViewedProducts(HttpServletRequest request) {
        HttpSessionAttributesHolder attributesHolder = new HttpSessionAttributesHolder(request.getSession());
        return recentlyViewedProductService.getRecentlyViewedProducts(attributesHolder);
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
