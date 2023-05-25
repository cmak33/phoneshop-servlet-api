package com.es.phoneshop.web.servlets;

import com.es.phoneshop.exception.CustomParseException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.parser.QuantityParser;
import com.es.phoneshop.model.validator.QuantityValidator;
import com.es.phoneshop.service.cart.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartAddItemServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private CartService cartService;
    @Mock
    private QuantityParser quantityParser;
    @Mock
    private QuantityValidator quantityValidator;
    @InjectMocks
    private final CartAddItemServlet cartServlet = new CartAddItemServlet();
    private final Locale locale = Locale.ENGLISH;

    @Before
    public void setup() {
        when(request.getLocale()).thenReturn(locale);
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
        String message = "Quantity was not a number";
        String redirectionPath = "/path";
        when(request.getParameter("quantity")).thenReturn(quantity);
        when(request.getParameter("redirectionPath")).thenReturn(redirectionPath);
        when(request.getPathInfo()).thenReturn(String.format("/%d", id));
        doAnswer(invocation -> {
            addErrorToMap(invocation, message);
            return false;
        })
                .when(quantityValidator)
                .validate(eq(quantity), eq(locale), any(), eq(id));
        redirectionPath += '?';
        String expectedRedirect = createErrorRedirectUrl(redirectionPath, id, message);

        cartServlet.doPost(request, response);

        verify(response).sendRedirect(expectedRedirect);
    }

    private void addErrorToMap(InvocationOnMock invocation, String message) {
        Map<Long, String> errorsMap = invocation.getArgument(2);
        Long productId = invocation.getArgument(3);
        errorsMap.put(productId, message);
    }

    @Test
    public void givenQuantityOutOfStockBounds_whenDoPost_thenSendErrorRedirect() throws IOException, OutOfStockException, CustomParseException {
        long id = 1L;
        int quantity = 2;
        int stock = 1;
        String redirectionPath = "/path";
        OutOfStockException outOfStockException = new OutOfStockException(quantity, stock);
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(quantityValidator.validate(eq(String.valueOf(quantity)), eq(locale), any(), eq(id))).thenReturn(true);
        when(quantityParser.parse(String.valueOf(quantity), locale)).thenReturn(quantity);
        when(request.getPathInfo()).thenReturn(String.format("/%d", id));
        when(request.getParameter("redirectionPath")).thenReturn(redirectionPath);
        doThrow(outOfStockException).when(cartService).addCartItem(any(), eq(id), eq(quantity));
        redirectionPath += '?';
        String expectedRedirect = createErrorRedirectUrl(redirectionPath, id, outOfStockException.getMessage());

        cartServlet.doPost(request, response);

        verify(response).sendRedirect(expectedRedirect);
    }

    @Test
    public void givenValidQuantity_whenDoPost_thenSendSuccessRedirect() throws IOException, OutOfStockException, CustomParseException {
        long id = 1L;
        int quantity = 2;
        String redirectionPath = "/path?param=1";
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(quantityValidator.validate(eq(String.valueOf(quantity)), eq(locale), any(), eq(id))).thenReturn(true);
        when(quantityParser.parse(String.valueOf(quantity), locale)).thenReturn(quantity);
        when(request.getPathInfo()).thenReturn(String.format("/%d", id));
        when(request.getParameter("redirectionPath")).thenReturn(redirectionPath);
        redirectionPath += "&";
        String expectedRedirect = createSuccessRedirectUrl(redirectionPath, id, quantity);

        cartServlet.doPost(request, response);

        verify(cartService).addCartItem(any(), eq(id), eq(quantity));
        verify(response).sendRedirect(expectedRedirect);
    }

    private String createErrorRedirectUrl(String redirectionUrl, Long productId, String message) {
        String query = String.format("errorMessage=%s&id=%d", message, productId);
        return redirectionUrl.concat(query);
    }

    private String createSuccessRedirectUrl(String redirectionUrl, Long productId, int quantity) {
        String successMessage = "Product was successfully added to cart";
        String query = String.format("message=%s&id=%d&quantity=%d", successMessage, productId, quantity);
        return redirectionUrl.concat(query);
    }
}
