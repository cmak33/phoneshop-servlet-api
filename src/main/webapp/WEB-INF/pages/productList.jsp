<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.List" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <form>
        <input type="text" name="query" value="${param.query}">
        <input type="submit" value="Search">
    </form>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
                <tags:sortLink field="description" order="ascending" additionalParams="query=${param.query}" text="↑"/>
                <tags:sortLink field="description" order="descending" additionalParams="query=${param.query}" text="↓"/>
            </td>
            <td class="price">
                Price
                <tags:sortLink field="price" order="ascending" additionalParams="query=${param.query}" text="↑"/>
                <tags:sortLink field="price" order="descending" additionalParams="query=${param.query}" text="↓"/>
            </td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>${product.description}</td>
                <td class="price">
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
        </c:forEach>
    </table>
</tags:master>