package com.es.phoneshop.web.servlets;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.CartProduct;
import com.es.phoneshop.model.parser.QuantityValidator;
import com.es.phoneshop.service.cart.CartService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    private RequestDispatcher requestDispatcher;
    @Mock
    private CartService cartService;
    @Mock
    private QuantityValidator quantityValidator;
    @InjectMocks
    private final CartServlet cartServlet = new CartServlet();
    private final Locale locale = Locale.ENGLISH;

    @Before
    public void setup() {
        when(request.getLocale()).thenReturn(locale);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void givenValidRequest_whenDoGet_thenForward() throws ServletException, IOException {
        List<CartProduct> cartProducts = new ArrayList<>() {
        };
        when(cartService.getCartProducts(any())).thenReturn(cartProducts);

        cartServlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void givenValidParameters_whenDoPost_thenRedirect() throws ServletException, IOException {
        String[] productsId = {"1", "2"};
        String[] quantities = {"1", "100"};
        when(request.getParameterValues("productId")).thenReturn(productsId);
        when(request.getParameterValues("quantity")).thenReturn(quantities);
        String expectedUrl = createSuccessUrl(request);

        cartServlet.doPost(request, response);

        verify(response).sendRedirect(expectedUrl);
    }

    @Test
    public void givenInvalidUpdateData_whenDoPost_thenSetErrorToAttributes() throws ServletException, IOException, OutOfStockException {
        Long productWithErrorId = 1L;
        int quantity = 1;
        OutOfStockException exception = new OutOfStockException(1, 0);
        Map<Long, String> errors = new HashMap<>();
        errors.put(productWithErrorId, exception.getMessage());
        String[] productsId = {String.valueOf(productWithErrorId), "2"};
        String[] quantities = {String.valueOf(quantity), "100"};
        when(request.getParameterValues("productId")).thenReturn(productsId);
        when(request.getParameterValues("quantity")).thenReturn(quantities);
        when(quantityValidator.tryParse(any(), eq(productWithErrorId), eq(locale), eq(quantities[0]))).thenReturn(Optional.of(quantity));
        doThrow(exception).when(cartService).updateItem(any(), eq(productWithErrorId), eq(quantity));

        cartServlet.doPost(request, response);

        verify(request).setAttribute("errors", errors);
        verify(requestDispatcher).forward(request, response);
    }

    private String createSuccessUrl(HttpServletRequest request) {
        return String.format("%s/cart?message=Cart was updated successfully", request.getContextPath());
    }
}
