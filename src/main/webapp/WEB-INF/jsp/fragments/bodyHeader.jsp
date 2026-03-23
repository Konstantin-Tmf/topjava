<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<header>
    <a href="<c:url value='/meals'/>"><spring:message code="app.title"/></a> |
    <a href="<c:url value='/users'/>"><spring:message code="user.title"/></a> |
    <a href="<c:url value='/'/>"><spring:message code="app.home"/></a>
</header>
