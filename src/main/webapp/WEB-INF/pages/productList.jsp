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
    <input type="hidden" id="products" value='${json}'>
    <p>
        Welcome to Expert-Soft training!
    </p>
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
                <tags:sortLink contextPath="${pageContext.servletContext.contextPath}" field="description"
                               order="ascending" additionalParams="query=${param.query}" text="↑"/>
                <tags:sortLink contextPath="${pageContext.servletContext.contextPath}" field="description"
                               order="descending" additionalParams="query=${param.query}" text="↓"/>
            </td>
            <td class="price">
                Price
                <tags:sortLink contextPath="${pageContext.servletContext.contextPath}" field="price" order="ascending"
                               additionalParams="query=${param.query}" text="↑"/>
                <tags:sortLink contextPath="${pageContext.servletContext.contextPath}" field="price" order="descending"
                               additionalParams="query=${param.query}" text="↓"/>
            </td>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}" alt="no image">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.getId()}">${product.description}</a>
                </td>
                <td class="price">
                    <a href="${pageContext.servletContext.contextPath}/price-history/${product.id}"><fmt:formatNumber
                            value="${product.price}"
                            type="currency" currencySymbol="${product.currency.symbol}"/></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</tags:master>