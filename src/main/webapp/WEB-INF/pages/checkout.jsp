<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Checkout page">
    <c:set var="contextPath" value="${pageContext.servletContext.contextPath}"/>
    <c:if test="${not empty errors}">
        <p style="color : red">There were errors during update</p>
    </c:if>
    <c:if test="${not isEmpty}">
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
        <form method="post">
            <table>
                <tags:orderFormRow errors="${errors}" label="First name:" name="firstName"
                                   value="${order['contactDetails']['firstName']}"/>
                <tags:orderFormRow errors="${errors}" label="Second name:" name="secondName"
                                   value="${order['contactDetails']['secondName']}"/>
                <tags:orderFormRow errors="${errors}" label="Phone:" name="phone"
                                   value="${order['contactDetails']['phone']}"/>
                <tags:orderFormRow errors="${errors}" label="Delivery address:" name="deliveryAddress"
                                   value="${order['deliveryDetails']['deliveryAddress']}"/>
                <tags:orderFormRow errors="${errors}" label="Delivery date:" name="deliveryDate" type="date"
                                   value="${order['deliveryDetails']['deliveryDate']}"/>
                <tr>
                    <td>
                        Payment method:
                    </td>
                    <td>
                        <select name="paymentMethod">
                            <c:forEach var="paymentMethod" items="${paymentMethods}">
                                <option>${paymentMethod}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
            </table>
            <button>Order</button>
        </form>
    </c:if>
    <c:if test="${isEmpty}">
        <h2>Your cart is empty</h2>
    </c:if>
    <a href="${contextPath}/products">Back to product list</a>
    <a href="${contextPath}/cart">Cart</a>
</tags:master>
