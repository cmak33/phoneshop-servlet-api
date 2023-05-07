package com.es.phoneshop.web;

import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.ProductService;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ProductService productService;
    @InjectMocks
    private final ProductDetailsPageServlet productDetailsPageServlet = new ProductDetailsPageServlet();

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void givenValidCode_whenDoGet_thenSetProductToAttribute() throws ServletException, IOException {
        Product product = new Product();
        product.setCode("code");
        when(request.getPathInfo()).thenReturn(String.format("/%s", product.getCode()));
        when(productService.getProductByCode(product.getCode())).thenReturn(product);

        productDetailsPageServlet.doGet(request, response);

        verify(productService).getProductByCode(product.getCode());
        verify(request).setAttribute("product", product);
        verify(requestDispatcher).forward(request, response);
    }

    @Test(expected = ProductNotFoundException.class)
    public void givenInvalidCode_whenDoGet_thenThrowProductNotFoundException() throws ServletException, IOException {
        String code = "code";
        when(request.getPathInfo()).thenReturn(String.format("/%s", code));
        when(productService.getProductByCode(code)).thenThrow(new ProductNotFoundException(code));

        productDetailsPageServlet.doGet(request, response);
    }
}
