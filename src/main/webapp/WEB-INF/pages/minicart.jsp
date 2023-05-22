<%@ page import="com.es.phoneshop.model.cart.Cart" %>
<%@ page import="com.es.phoneshop.utility.SessionAttributeNames" %>

<%
    String cartName = SessionAttributeNames.CART_ATTRIBUTE_NAME;
    Cart cart = (Cart) request.getSession().getAttribute(cartName);
%>
<div class="minicart">Cart : <%= cart.getTotalQuantity() %> products with total price
    of <%= cart.getTotalCost() == null ? 0 : cart.getTotalCost() %>
</div>