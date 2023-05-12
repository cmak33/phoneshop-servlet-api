<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product details">
    <c:if test="${not empty param.errorMessage}">
        <p style="color: red">${param.errorMessage}</p>
    </c:if>
    <c:if test="${not empty param.message}">
        <p style="color: green">${param.message}</p>
    </c:if>
    <c:forEach var="item" items="${cart.cartItems}">
        <p>${item.productId} : ${item.quantity}</p>
    </c:forEach>
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
    <form method="POST" action="${pageContext.servletContext.contextPath}/add-product-to-cart/${product.id}">
        <label for="quantity">quantity</label>
        <input id="quantity" type="number" value="${not empty param.quantity ? param.quantity : 1}" name="quantity">
        <input type="submit" value="Add to cart">
    </form>
    <a href="${pageContext.servletContext.contextPath}/products">Back to product list</a>
</tags:master>
