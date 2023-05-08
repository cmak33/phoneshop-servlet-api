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
public class ProductPriceHistoryPageServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ProductService productService;
    @InjectMocks
    private final ProductPriceHistoryPageServlet servlet = new ProductPriceHistoryPageServlet();

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test(expected = ProductNotFoundException.class)
    public void givenNonExistingIdForProductWithPriceHistory_whenDoGet_thenThrowProductNotFoundException() throws ServletException, IOException {
        long id = 1L;
        when(request.getPathInfo()).thenReturn(String.format("/%d", id));
        when(productService.getProduct(id)).thenThrow(new ProductNotFoundException(id));

        servlet.doGet(request, response);
    }

    @Test(expected = NumberFormatException.class)
    public void givenIdThatCanNotBeCastedToNumber_whenDoGet_thenThrowNumberFormatException() throws ServletException, IOException {
        String id = "invalid id";
        when(request.getPathInfo()).thenReturn(id);

        servlet.doGet(request, response);
    }

    @Test
    public void givenValidIdForProductWithPriceHistory_whenDoGet_thenSetProductToAttribute() throws ServletException, IOException {
        Product product = new Product();
        product.setId(1L);
        when(request.getPathInfo()).thenReturn(String.format("/%d", product.getId()));
        when(productService.getProduct(product.getId())).thenReturn(product);

        servlet.doGet(request, response);

        verify(productService).getProduct(product.getId());
        verify(request).setAttribute("product", product);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void givenNullPathInfo_whenDoGet_thenSetResponseStatusToNotFound() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}
