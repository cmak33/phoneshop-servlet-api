package com.es.phoneshop.web.servlets;

import com.es.phoneshop.exception.notFoundException.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.order.OrderService;
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
public class OrderOverviewServletTest {

    @Mock
    private OrderService orderService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @InjectMocks
    private final OrderOverviewPageServlet orderOverviewPageServlet = new OrderOverviewPageServlet();

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void givenNullPathInfo_whenDoGet_thenSendError() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(null);

        orderOverviewPageServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test(expected = OrderNotFoundException.class)
    public void givenInvalidId_whenDoGet_thenThrowOrderNotFoundException() throws ServletException, IOException {
        Long invalidId = -1L;
        when(request.getPathInfo()).thenReturn(String.format("/%d", invalidId));
        when(orderService.getOrder(invalidId)).thenThrow(new OrderNotFoundException(invalidId));

        orderOverviewPageServlet.doGet(request, response);
    }

    @Test
    public void givenValidId_whenDoGet_thenForward() throws ServletException, IOException {
        Order order = new Order();
        Long id = 1L;
        when(request.getPathInfo()).thenReturn(String.format("/%d", id));
        when(orderService.getOrder(id)).thenReturn(order);

        orderOverviewPageServlet.doGet(request, response);

        verify(request).setAttribute("order", order);
        verify(requestDispatcher).forward(request, response);
    }
}
