<%@ page import="com.es.phoneshop.service.cart.CustomCartService" %>
<%@ page import="com.es.phoneshop.model.cart.Cart" %>

<%
    String cartName = CustomCartService.CART_ATTRIBUTE_NAME;
    Cart cart = (Cart) request.getSession().getAttribute(cartName);
%>
<div class="minicart">Cart : <%= cart.getTotalQuantity() %> products with total price
    of <%= cart.getTotalCost() == null ? 0 : cart.getTotalCost() %>
</div>