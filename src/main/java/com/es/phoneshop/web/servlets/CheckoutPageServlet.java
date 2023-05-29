package com.es.phoneshop.web.servlets;

import com.es.phoneshop.model.attributesHolder.AttributesHolder;
import com.es.phoneshop.model.attributesHolder.HttpSessionAttributesHolder;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.contactDetails.ContactDetails;
import com.es.phoneshop.model.deliveryDetails.DeliveryDetails;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.paymentMethod.PaymentMethod;
import com.es.phoneshop.validator.localDateValidator.DeliveryDateValidator;
import com.es.phoneshop.validator.localDateValidator.LocalDateValidator;
import com.es.phoneshop.validator.parameterValidator.DefaultParameterValidator;
import com.es.phoneshop.validator.parameterValidator.PhoneParameterValidator;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.CustomCartService;
import com.es.phoneshop.service.order.CustomOrderService;
import com.es.phoneshop.service.order.OrderService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CheckoutPageServlet extends HttpServlet {

    private CartService cartService;
    private OrderService orderService;
    private PhoneParameterValidator phoneParameterValidator;
    private DefaultParameterValidator defaultParameterValidator;
    private LocalDateValidator localDateValidator;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CustomCartService.getInstance();
        orderService = CustomOrderService.getInstance();
        phoneParameterValidator = PhoneParameterValidator.getInstance();
        defaultParameterValidator = DefaultParameterValidator.getInstance();
        localDateValidator = DeliveryDateValidator.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AttributesHolder attributesHolder = new HttpSessionAttributesHolder(request.getSession());
        Cart cart = cartService.getCart(attributesHolder);
        if (!cart.getCartItems().isEmpty()) {
            if (request.getAttribute("order") == null) {
                Order order = orderService.createOrder(cart);
                request.setAttribute("order", order);
            }
            request.setAttribute("products", cartService.getCartProducts(attributesHolder));
            request.setAttribute("paymentMethods", PaymentMethod.getMethodNames());
            request.setAttribute("isEmpty", false);
        } else {
            request.setAttribute("isEmpty", true);
        }
        request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AttributesHolder attributesHolder = new HttpSessionAttributesHolder(request.getSession());
        Cart cart = cartService.getCart(attributesHolder);
        Order order = orderService.createOrder(cart);
        Map<String, String> errors = new HashMap<>();
        setContactDetails(request, errors, order);
        setDeliveryDetails(request, errors, order);
        setPaymentMethod(errors, request, "paymentMethod", order);
        if (errors.isEmpty()) {
            cartService.clearCart(attributesHolder);
            orderService.placeOrder(order);
            String url = String.format("%s/order/overview/%s", request.getContextPath(), order.getId());
            response.sendRedirect(url);
        } else {
            request.setAttribute("order", order);
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }

    private void setContactDetails(HttpServletRequest request, Map<String, String> errors, Order order) {
        ContactDetails contactDetails = new ContactDetails();
        order.setContactDetails(contactDetails);
        setNotBlankParameter(errors, request, "firstName", contactDetails::setFirstName);
        setNotBlankParameter(errors, request, "secondName", contactDetails::setSecondName);
        setPhoneNumber(errors, request, "phone", contactDetails);
    }

    private void setDeliveryDetails(HttpServletRequest request, Map<String, String> errors, Order order) {
        DeliveryDetails deliveryDetails = new DeliveryDetails();
        order.setDeliveryDetails(deliveryDetails);
        setNotBlankParameter(errors, request, "deliveryAddress", deliveryDetails::setDeliveryAddress);
        setOrderDate(errors, request, "deliveryDate", deliveryDetails);
    }

    private void setOrderDate(Map<String, String> errors, HttpServletRequest request, String parameterName, DeliveryDetails deliveryDetails) {
        LocalDate date;
        try {
            date = LocalDate.parse(request.getParameter(parameterName));
        } catch (DateTimeParseException dateTimeParseException) {
            errors.put(parameterName, "Not a valid date");
            return;
        }
        if (localDateValidator.validate(date, errors, parameterName)) {
            deliveryDetails.setDeliveryDate(date);
        }
    }

    private void setPaymentMethod(Map<String, String> errors, HttpServletRequest request, String parameterName, Order order) {
        PaymentMethod paymentMethod = PaymentMethod.parseFromMethodName(request.getParameter(parameterName));
        if (paymentMethod != null) {
            order.setPaymentMethod(paymentMethod);
        } else {
            errors.put(parameterName, "Not valid payment method");
        }
    }

    private void setPhoneNumber(Map<String, String> errors, HttpServletRequest request, String parameterName, ContactDetails contactDetails) {
        if (phoneParameterValidator.validate(request.getParameter(parameterName), errors, parameterName)) {
            contactDetails.setPhone(request.getParameter(parameterName));
        }
    }

    private void setNotBlankParameter(Map<String, String> errors, HttpServletRequest request, String parameterName, Consumer<String> consumer) {
        if (defaultParameterValidator.validate(request.getParameter(parameterName), errors, parameterName)) {
            consumer.accept(request.getParameter(parameterName));
        }
    }
}
