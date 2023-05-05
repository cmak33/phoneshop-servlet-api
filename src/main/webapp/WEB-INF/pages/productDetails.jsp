<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product details">
    <table>
        <tr>
            <td>Image</td>
            <td>
                <img class="product-tile" src="${product.imageUrl}" alt="no image">
            </td>
        </tr>
        <tr>
            <td>
                Description
            </td>
            <td>
                    ${product.description}
            </td>
        </tr>
        <tr>
            <td>
                Price
            </td>
            <td>
                <fmt:formatNumber value="${product.price}" type="currency"
                                  currencySymbol="${product.currency.symbol}"/>
            </td>
        </tr>
        <tr>
            <td>
                Code
            </td>
            <td>
                    ${product.code}
            </td>
        </tr>
        <tr>
            <td>
                Stock
            </td>
            <td>
                    ${product.stock}
            </td>
        </tr>
    </table>
    <a href="${pageContext.servletContext.contextPath}/products">Back to product list</a>
</tags:master>
