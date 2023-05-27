<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.List" scope="request"/>
<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>

<tags:master pageTitle="Order overview">
    <c:set var="contextPath" value="${pageContext.servletContext.contextPath}"/>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
            </td>
            <td>
                Quantity
            </td>
            <td class="price">
                Price
            </td>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${products}" varStatus="status">
            <c:set var="product" value="${item.product()}"/>
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}" alt="no image">
                </td>
                <td>
                    <a href="${contextPath}/products/${product.getId()}">${product.description}</a>
                </td>
                <td>
                    <p>${item.quantity()}</p>
                </td>
                <td class="price">
                    <a href="${contextPath}/price-history/${product.id}"><fmt:formatNumber
                            value="${product.price}"
                            type="currency" currencySymbol="${product.currency.symbol}"/></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <table>
        <tr>
            <td>Subtotal:</td>
            <td>
                    ${order.subtotal}
            </td>
        </tr>
        <tr>
            <td>Delivery cost:</td>
            <td>
                    ${order.deliveryCost}
            </td>
        </tr>
        <tr>
            <td>Total cost:</td>
            <td>
                    ${order.totalCost}
            </td>
        </tr>
    </table>
    <h2>Your details</h2>
    <table>
        <tags:orderOverviewRow label="First name:"
                               value="${order['contactDetails']['firstName']}"/>
        <tags:orderOverviewRow label="Second name:"
                               value="${order['contactDetails']['secondName']}"/>
        <tags:orderOverviewRow label="Phone:"
                               value="${order['contactDetails']['phone']}"/>
        <tags:orderOverviewRow label="Delivery address:"
                               value="${order['deliveryDetails']['deliveryAddress']}"/>
        <tags:orderOverviewRow label="Delivery date:"
                               value="${order['deliveryDetails']['deliveryDate']}"/>
        <tags:orderOverviewRow label="Payment method:"
                               value="${order.getPaymentMethod().getMethodName()}"/>
    </table>
    <a href="${contextPath}/products">Back to product list</a>
    <a href="${contextPath}/cart">Cart</a>
</tags:master>

