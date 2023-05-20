<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.List" scope="request"/>

<tags:master pageTitle="Cart">
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
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${products}">
            <tr>
                <td>
                    <img class="product-tile" src="${item.product().imageUrl}" alt="no image">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${item.product().getId()}">${item.product().description}</a>
                </td>
                <td>
                    <input type="text" name="quantity" value="${item.quantity()}">
                    <input type="hidden" name="id" value="${item.product().id}">
                </td>
                <td class="price">
                    <a href="${pageContext.servletContext.contextPath}/price-history/${item.product().id}"><fmt:formatNumber
                            value="${item.product().price}"
                            type="currency" currencySymbol="${item.product().currency.symbol}"/></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</tags:master>
