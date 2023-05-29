<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="value" required="true" %>
<%@ attribute name="errors" type="java.util.Map" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="type" required="false" %>

<c:if test="${empty type}">
    <c:set var="type" value="text"/>
</c:if>

<tr>
    <td>
        ${label}
        <span style="color: red">*</span>
    </td>
    <td>
        <c:set var="error" value="${errors[name]}"/>
        <input name="${name}" type="${type}" value="${not empty error ? '' : value}">
        <c:if test="${not empty error}">
            <p style="color : red">${error}</p>
        </c:if>
    </td>
</tr>

