<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.List" scope="request"/>

<tags:master pageTitle="Product List">
    <c:set var="contextPath" value="${pageContext.servletContext.contextPath}"/>
    <a href="${contextPath}/products">Back to main page</a>
    <form method="get">
        <label for="description">Description</label>
        <input type="text" id="description" name="description" value="${param.description}">
        <select name="searchType">
            <option value="all_words" ${param.searchType == 'all_words' ? 'selected' : ''}>All words</option>
            <option value="any_word" ${param.searchType == 'any_word' ? 'selected' : ''}>Any word</option>
        </select>
        <tags:formField label="Min price" errors="${errors}"
                        name="minPrice" value="${param.minPrice}"/>
        <tags:formField label="Max price" errors="${errors}"
                        name="maxPrice" value="${param.maxPrice}"/>
        <input type="submit" value="Search">
    </form>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
            </td>
            <td class="price">
                Price
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
                    <a href="${contextPath}/products/${product.getId()}">${product.description}</a>
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
</tags:master>