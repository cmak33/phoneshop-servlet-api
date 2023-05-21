package com.es.phoneshop.web.servlets;

import com.es.phoneshop.exception.ProductNotInCartException;
import com.es.phoneshop.service.cart.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartDeleteItemServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private CartService cartService;
    @InjectMocks
    private final CartDeleteItemServlet cartDeleteItemServlet = new CartDeleteItemServlet();

    @Test
    public void givenInvalidPathInfo_whenDoPost_thenSend404Error() throws IOException {
        String pathInfo = "";
        when(request.getPathInfo()).thenReturn(pathInfo);

        cartDeleteItemServlet.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void givenNonExistingId_whenDoPost_thenSend404Error() throws IOException {
        Long id = 1L;
        when(request.getPathInfo()).thenReturn(String.format("/%d", id));
        doThrow(new ProductNotInCartException(id)).when(cartService).deleteItem(any(), eq(id));

        cartDeleteItemServlet.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void givenValidId_whenDoPost_thenRedirect() throws IOException {
        Long id = 1L;
        String contextPath = "contextPath";
        when(request.getContextPath()).thenReturn(contextPath);
        when(request.getPathInfo()).thenReturn(String.format("/%d", id));
        String expectedUrl = createSuccessUrl(request);

        cartDeleteItemServlet.doPost(request, response);

        verify(response).sendRedirect(expectedUrl);
    }

    private String createSuccessUrl(HttpServletRequest request) {
        return String.format("%s/cart?message=Item was successfully deleted", request.getContextPath());
    }
}
