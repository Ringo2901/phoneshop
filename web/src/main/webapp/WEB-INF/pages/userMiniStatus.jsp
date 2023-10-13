<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<security:authorize access="hasRole('ADMIN')">
    <button class="btn btn-light" onclick="window.location.href='<c:url value="/admin/orders"/>'">Admin</button>
</security:authorize>
<security:authorize access="isAuthenticated()">
    <button class="btn btn-light" onclick="window.location.href='<c:url value="/logout"/>'">Logout</button>
</security:authorize>
<security:authorize access="!isAuthenticated()">
    <button class="btn btn-light" onclick="window.location.href='<c:url value="/signIn"/>'">Login</button>
</security:authorize>
