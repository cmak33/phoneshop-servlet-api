package com.es.phoneshop.web.servlets;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.ProductAndQuantity;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.paymentMethod.PaymentMethod;
import com.es.phoneshop.model.validator.localDateValidator.LocalDateValidator;
import com.es.phoneshop.model.validator.parameterValidator.NotBlankParameterValidator;
import com.es.phoneshop.model.validator.parameterValidator.PhoneParameterValidator;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.order.OrderService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private CartService cartService;
    @Mock
    private OrderService orderService;
    @Mock
    private PhoneParameterValidator phoneParameterValidator;
    @Mock
    private NotBlankParameterValidator notBlankParameterValidator;
    @Mock
    private LocalDateValidator localDateValidator;
    @Mock
    private Cart cart;
    @Mock
    private HttpSession httpSession;
    @Mock
    private Order order;
    @InjectMocks
    private final CheckoutPageServlet checkoutPageServlet = new CheckoutPageServlet();

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(httpSession);
    }

    @Test
    public void givenNotEmptyCart_whenDoGet_thenForward() throws ServletException, IOException {
        List<ProductAndQuantity> productList = new ArrayList<>();
        when(cartService.getCart(any())).thenReturn(cart);
        when(cart.getCartItems()).thenReturn(new ArrayList<>() {{
            add(new CartItem(1L, 1));
        }});
        when(orderService.createOrder(cart)).thenReturn(order);

        checkoutPageServlet.doGet(request, response);

        verify(request).setAttribute("isEmpty", false);
        verify(request).setAttribute("order", order);
        verify(request).setAttribute("products", productList);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void givenEmptyCart_whenDoGet_thenSetEmptyAttributeToTrue() throws ServletException, IOException {
        when(cartService.getCart(any())).thenReturn(cart);
        when(cart.getCartItems()).thenReturn(new ArrayList<>());

        checkoutPageServlet.doGet(request, response);

        verify(request).setAttribute("isEmpty", true);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void givenValidOrderParameters_whenDoPost_thenPlaceOrder() throws ServletException, IOException {
        when(cartService.getCart(any())).thenReturn(cart);
        when(orderService.createOrder(cart)).thenReturn(order);
        when(notBlankParameterValidator.validate(any(), any(), any())).thenReturn(true);
        when(phoneParameterValidator.validate(any(), any(), any())).thenReturn(true);
        when(localDateValidator.validate(any(), any(), anyString())).thenReturn(true);
        when(request.getParameter("deliveryDate")).thenReturn(LocalDate.now().toString());
        when(request.getParameter("paymentMethod")).thenReturn(PaymentMethod.CASH.getMethodName());

        checkoutPageServlet.doPost(request, response);

        verify(orderService).placeOrder(order);
        verify(response).sendRedirect(any());
    }

    @Test
    public void givenInvalidOrderParameters_whenDoPost_thenSetErrors() throws ServletException, IOException {
        LocalDate date = LocalDate.now();
        when(cartService.getCart(any())).thenReturn(cart);
        when(orderService.createOrder(cart)).thenReturn(order);
        when(notBlankParameterValidator.validate(any(), any(), any())).thenReturn(true);
        when(phoneParameterValidator.validate(any(), any(), any())).thenReturn(true);
        doAnswer(mock -> {
            Map<String, String> errors = mock.getArgument(1);
            String parameterName = mock.getArgument(2);
            errors.put(parameterName, "There were an error");
            return false;
        }).when(localDateValidator).validate(any(), any(), any());
        when(request.getParameter("deliveryDate")).thenReturn(date.toString());
        when(request.getParameter("paymentMethod")).thenReturn(PaymentMethod.CASH.getMethodName());

        checkoutPageServlet.doPost(request, response);

        verify(orderService, never()).placeOrder(order);
        verify(request).setAttribute(eq("errors"), any());
        verify(requestDispatcher).forward(request, response);
    }

}
