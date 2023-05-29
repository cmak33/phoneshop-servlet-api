<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.es.phoneshop.model.cart.Cart" %>
<%@ page import="com.es.phoneshop.utility.SessionAttributeNames" %>

<%
    String cartName = SessionAttributeNames.CART_ATTRIBUTE_NAME;
    Cart cart = (Cart) request.getSession().getAttribute(cartName);
%>
<a href="${pageContext.servletContext.contextPath}/cart" class="minicart">Cart(<%= cart.getTotalQuantity() %>) -
    <%= cart.getTotalCost()%>
</a>