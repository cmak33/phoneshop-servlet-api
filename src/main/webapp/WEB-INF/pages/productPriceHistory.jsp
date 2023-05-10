<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product price history">
    <p>Price history of ${product.description}</p>
    <table>
        <thead>
        <tr>
            <td>Start date</td>
            <td>Price</td>
        </tr>
        </thead>
        <c:forEach var="priceChange" items="${product.priceHistory}">
            <tr>
                <td>
                        ${priceChange.dateOfChange()}
                </td>
                <td>
                        ${priceChange.price()}
                </td>
            </tr>
        </c:forEach>
    </table>
    <a href="${pageContext.servletContext.contextPath}/products">Back to product list</a>
</tags:master>
