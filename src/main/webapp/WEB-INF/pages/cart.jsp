<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.List" scope="request"/>

<tags:master pageTitle="Cart">
    <c:set var="contextPath" value="${pageContext.servletContext.contextPath}"/>
    <c:if test="${not empty param.message}">
        <p style="color : green">${param.message}</p>
    </c:if>
    <c:if test="${not empty errors}">
        <p style="color : red">There were errors during update</p>
    </c:if>
    <form method="post" action="${contextPath}/cart">
        <table id="productsTable">
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
                <td>
                    Delete item
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
                        <c:set var="error" value="${errors[product.id]}"/>
                        <input type="text" name="quantity" value="${empty error?item.quantity():''}">
                        <input type="hidden" name="productId" value="${product.id}">
                        <c:if test="${not empty error}">
                            <div style="color : red">${error}</div>
                        </c:if>
                    </td>
                    <td class="price">
                        <a href="${contextPath}/price-history/${product.id}"><fmt:formatNumber
                                value="${product.price}"
                                type="currency" currencySymbol="${product.currency.symbol}"/></a>
                    </td>
                    <td>
                        <button form="deleteItem"
                                formaction="${contextPath}/cart/delete-item/${product.id}">
                            delete
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <p>
            <input type="submit" value="update">
        </p>
    </form>
    <form method="post" id="deleteItem">
    </form>
    <a href="${contextPath}/products">Back to product list</a>
</tags:master>
