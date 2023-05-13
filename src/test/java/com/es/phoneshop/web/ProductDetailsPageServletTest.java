package com.es.phoneshop.web;

import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.product.ProductService;
import com.es.phoneshop.service.product.recentlyViewedProducts.RecentlyViewedProductService;
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
    @Mock
    private RecentlyViewedProductService recentlyViewedProductService;
    @InjectMocks
    private final ProductDetailsPageServlet productDetailsPageServlet = new ProductDetailsPageServlet();

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void givenValidId_whenDoGet_thenSetProductToAttribute() throws ServletException, IOException {
        Product product = new Product();
        product.setId(1L);
        when(request.getPathInfo()).thenReturn(String.format("/%d", product.getId()));
        when(productService.getProduct(product.getId())).thenReturn(product);

        productDetailsPageServlet.doGet(request, response);

        verify(productService).getProduct(product.getId());
        verify(request).setAttribute("product", product);
        verify(requestDispatcher).forward(request, response);
    }

    @Test(expected = ProductNotFoundException.class)
    public void givenIdOfNonExistingProduct_whenDoGet_thenThrowProductNotFoundException() throws ServletException, IOException {
        long id = 1L;
        when(request.getPathInfo()).thenReturn(String.format("/%d", id));
        when(productService.getProduct(id)).thenThrow(new ProductNotFoundException(id));

        productDetailsPageServlet.doGet(request, response);
    }

    @Test
    public void givenNullPathInfo_whenDoGet_thenSetResponseStatusToNotFound() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(null);

        productDetailsPageServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
