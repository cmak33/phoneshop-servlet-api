package com.es.phoneshop.web.servlets;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.product.ProductService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ProductService productService;
    @InjectMocks
    private final ProductListPageServlet servlet = new ProductListPageServlet();
    private List<Product> productList;

    @Before
    public void setup() {
        productList = createProductList();
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    private List<Product> createProductList() {
        return new ArrayList<>() {{
            add(new Product());
        }};
    }

    @Test
    public void givenValidDescription_whenDoGet_thenSetProductsAttribute() throws ServletException, IOException {
        String description = "query";
        when(request.getParameter("query")).thenReturn(description);
        when(productService.findProductsByDescription(description)).thenReturn(productList);

        servlet.doGet(request, response);

        verify(productService).findProductsByDescription(description);
        verify(request).setAttribute("products", productList);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void givenNullDescription_whenDoGet_thenSetProductsAttribute() throws ServletException, IOException {
        when(productService.findProducts()).thenReturn(productList);

        servlet.doGet(request, response);

        verify(productService).findProducts();
        verify(request).setAttribute("products", productList);
        verify(requestDispatcher).forward(request, response);
    }
}