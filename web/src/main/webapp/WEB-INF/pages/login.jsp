<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="frm" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tags:master pageTitle="Login">
    <c:if test="${not empty param.error}">
        <div class="container">
            <div class="panel panel-danger">
                <div class="panel-heading">Error</div>
                <div class="panel-body">Bad credits</div>
            </div>
        </div>
    </c:if>
    <div class="container">
        <div class="row">
            <div class="col-4"></div>
            <div class="col-4 container-fluid">
                <form method="post" name="f" action="<c:url value="/login"/>">
                    <div class="row">
                        <div class="col-4">
                            Login:
                        </div>
                        <div class="col-8">
                            <input type="text" name="username">
                        </div>
                    </div>
                    <br>
                    <div class="row">
                        <div class="col-4">
                            Password:
                        </div>
                        <div class="col-8">
                            <input type="password" name="password">
                        </div>
                    </div>
                    <br>
                    <button class="btn btn-light float-right" type="submit" value="submit">Login</button>
                </form>
            </div>
            <div class="col-4"></div>
        </div>
    </div>
</tags:master>