<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="field" required="true" %>
<%@ attribute name="order" required="true" %>
<%@ attribute name="additionalParams" %>
<%@ attribute name="text" %>

<a href="?${additionalParams}&field=${field}&order=${order}"
   style="${field eq param.field and order eq param.order ? 'font-weight: bold' : ''}">${text == null?order:text}</a>
