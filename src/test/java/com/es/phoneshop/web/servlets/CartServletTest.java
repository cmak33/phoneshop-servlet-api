package com.es.phoneshop.web.servlets;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.service.cart.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private CartService cartService;
    @InjectMocks
    private final CartServlet cartServlet = new CartServlet();

    @Before
    public void setup() {
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
    }

    @Test
    public void givenInvalidPathInfo_whenDoPost_thenSend404Error() throws IOException {
        String pathInfo = "";
        when(request.getPathInfo()).thenReturn(pathInfo);

        cartServlet.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void givenNotNumberQuantity_whenDoPost_thenSendErrorRedirect() throws IOException {
        long id = 1L;
        String quantity = "not a number";
        when(request.getParameter("quantity")).thenReturn(quantity);
        when(request.getPathInfo()).thenReturn(String.format("/%d", id));
        String expectedRedirect = createErrorRedirectUrl(request, id, "Quantity was not a number");

        cartServlet.doPost(request, response);

        verify(response).sendRedirect(expectedRedirect);
    }

    @Test
    public void givenNegativeQuantity_whenDoPost_thenSendErrorRedirect() throws IOException {
        long id = 1L;
        int quantity = -1;
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getPathInfo()).thenReturn(String.format("/%d", id));
        String expectedRedirect = createErrorRedirectUrl(request, id, "Quantity should be bigger than zero");

        cartServlet.doPost(request, response);

        verify(response).sendRedirect(expectedRedirect);
    }

    @Test
    public void givenQuantityOutOfStockBounds_whenDoPost_thenSendErrorRedirect() throws IOException, OutOfStockException {
        long id = 1L;
        int quantity = 2;
        int stock = 1;
        OutOfStockException outOfStockException = new OutOfStockException(quantity, stock);
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getPathInfo()).thenReturn(String.format("/%d", id));
        doThrow(outOfStockException).when(cartService).addItem(any(), eq(id), eq(quantity));
        String expectedRedirect = createErrorRedirectUrl(request, id, outOfStockException.getMessage());

        cartServlet.doPost(request, response);

        verify(response).sendRedirect(expectedRedirect);
    }

    @Test
    public void givenValidQuantity_whenDoPost_thenSendSuccessRedirect() throws IOException, OutOfStockException {
        long id = 1L;
        int quantity = 2;
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getPathInfo()).thenReturn(String.format("/%d", id));
        String expectedRedirect = createSuccessRedirectUrl(request, id, quantity);

        cartServlet.doPost(request, response);

        verify(cartService).addItem(any(), eq(id), eq(quantity));
        verify(response).sendRedirect(expectedRedirect);
    }

    private String createErrorRedirectUrl(HttpServletRequest request, Long productId, String message) {
        return String.format("%s/products/%d?errorMessage=%s", request.getContextPath(), productId, message);
    }

    private String createSuccessRedirectUrl(HttpServletRequest request, Long productId, int quantity) {
        String successMessage = "Product was successfully added to cart";
        return String.format("%s/products/%d?message=%s&quantity=%d", request.getContextPath(), productId, successMessage, quantity);
    }
}
