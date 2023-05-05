<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="message" type="java.lang.String" scope="request"/>
<tags:master pageTitle="404 not found">
    <p>${message}</p>
    <a href="${pageContext.servletContext.contextPath}/products">Back to product list</a>
</tags:master>
