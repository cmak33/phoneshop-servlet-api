<%@ page isErrorPage="true" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:error errorType="404 not found" message="${pageContext.exception.getMessage()}"/>
