package com.es.phoneshop.web.servlets;

import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.order.CustomOrderService;
import com.es.phoneshop.service.order.OrderService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class OrderOverviewPageServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        orderService = CustomOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getPathInfo() != null && request.getPathInfo().length() > 1) {
            Long id = Long.parseLong(request.getPathInfo().substring(1));
            Order order = orderService.getOrder(id);
            request.setAttribute("order", order);
            request.setAttribute("products", orderService.getOrderProducts(order));
            request.getRequestDispatcher("/WEB-INF/pages/orderOverview.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
