<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.List" scope="request"/>

<%
    String json = new ObjectMapper().writeValueAsString(products);
    request.setAttribute("json", json);
%>

<head>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="${pageContext.servletContext.contextPath}/js/productSorter.js"></script>
</head>
<tags:master pageTitle="Product List">
    <c:set var="contextPath" value="${pageContext.servletContext.contextPath}"/>
    <input type="hidden" id="products" value='${json}'>
    <p>
        Welcome to Expert-Soft training!
    </p>
    <c:if test="${not empty param.errorMessage}">
        <p style="color: red">There were errors</p>
    </c:if>
    <c:if test="${not empty param.message}">
        <p style="color: green">${param.message}</p>
    </c:if>
    <a href="${contextPath}/cart">Cart</a>
    <a href="${contextPath}/products/advanced-search">Advanced search</a>
    <form>
        <input type="text" name="query" value="${param.query}">
        <input type="submit" value="Search">
    </form>
    <table id="productsTable">
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
                <tags:sortLink contextPath="${contextPath}" field="description"
                               order="ascending" additionalParams="query=${param.query}" text="↑"/>
                <tags:sortLink contextPath="${contextPath}" field="description"
                               order="descending" additionalParams="query=${param.query}" text="↓"/>
            </td>
            <td>
                Quantity
            </td>
            <td class="price">
                Price
                <tags:sortLink contextPath="${contextPath}" field="price" order="ascending"
                               additionalParams="query=${param.query}" text="↑"/>
                <tags:sortLink contextPath="${contextPath}" field="price" order="descending"
                               additionalParams="query=${param.query}" text="↓"/>
            </td>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="product" items="${products}">
            <tr>
                <form action="${contextPath}/add-product-to-cart/${product.id}"
                      method="post">
                    <input type="hidden" name="redirectionPath"
                           value="${contextPath}/products?query=${param.query}">
                    <td>
                        <img class="product-tile" src="${product.imageUrl}" alt="no image">
                    </td>
                    <td>
                        <a href="${contextPath}/products/${product.getId()}">${product.description}</a>
                    </td>
                    <td>
                        <input type="text" name="quantity" value="1">
                        <c:if test="${not empty param.errorMessage && param.id == product.id}">
                            <div style="color : red">${param.errorMessage}</div>
                        </c:if>
                    </td>
                    <td class="price">
                        <a href="${contextPath}/price-history/${product.id}"><fmt:formatNumber
                                value="${product.price}"
                                type="currency" currencySymbol="${product.currency.symbol}"/></a>
                    </td>
                    <td>
                        <input type="submit" value="Add to cart">
                    </td>
                </form>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</tags:master>