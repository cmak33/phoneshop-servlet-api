<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.List" scope="request"/>

<tags:master pageTitle="Cart">
    <c:if test="${not empty param.message}">
        <p style="color : green">${param.message}</p>
    </c:if>
    <c:if test="${not empty errors}">
        <p style="color : red">There were errors during update</p>
    </c:if>
    <form method="post" action="${pageContext.servletContext.contextPath}/cart">
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
                <tr>
                    <td>
                        <img class="product-tile" src="${item.product().imageUrl}" alt="no image">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${item.product().getId()}">${item.product().description}</a>
                    </td>
                    <td>
                        <c:set var="error" value="${errors[item.product().id]}"/>
                        <input type="text" name="quantity" value="${empty error?item.quantity():''}">
                        <input type="hidden" name="productId" value="${item.product().id}">
                        <c:if test="${not empty error}">
                            <div style="color : red">${error}</div>
                        </c:if>
                    </td>
                    <td class="price">
                        <a href="${pageContext.servletContext.contextPath}/price-history/${item.product().id}"><fmt:formatNumber
                                value="${item.product().price}"
                                type="currency" currencySymbol="${item.product().currency.symbol}"/></a>
                    </td>
                    <td>
                        <button form="deleteItem"
                                formaction="${pageContext.servletContext.contextPath}/cart/delete-item/${item.product().id}">
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
</tags:master>
