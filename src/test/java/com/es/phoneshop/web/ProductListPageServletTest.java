package com.es.phoneshop.web;

import com.es.phoneshop.model.product.CustomProductDao;
import com.es.phoneshop.model.product.Product;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
    private CustomProductDao customProductDao;

    private final ProductListPageServlet servlet = new ProductListPageServlet();

    @Before
    public void setup() {
        servlet.setProductDao(customProductDao);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        List<Product> productList = new ArrayList<>() {{
            add(new Product());
        }};
        when(customProductDao.findProducts()).thenReturn(productList);

        servlet.doGet(request, response);

        verify(request).setAttribute("products", productList);
        verify(requestDispatcher).forward(request, response);
    }
}