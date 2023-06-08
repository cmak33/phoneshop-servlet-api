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
<c:set var="error" value="${errors[name]}"/>

<label for="${name}">${label}</label>
<input id="${name}" name="${name}" type="${type}" value="${not empty error ? '' : value}">
<c:if test="${not empty error}">
    <p style="color : red">${error}</p>
</c:if>
