<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="errorType" required="true" %>
<%@ attribute name="message" required="true" %>

<tags:master pageTitle="${errorType}">
    <p>${message}</p>
    <a href="${pageContext.servletContext.contextPath}/products">Back to product list</a>
</tags:master>
