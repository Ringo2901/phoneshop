<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="frm" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Order">
    <p></p>
    <div id="statusMessage" class="container"><span></span></div>
    <c:if test="${not empty successMessage}">
        <div class="container">
            <div class="panel panel-success">
                <div class="panel-heading">Success</div>
                <div class="panel-body">${successMessage}</div>
            </div>
        </div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="container">
            <div class="panel panel-danger">
                <div class="panel-heading">Error</div>
                <div class="panel-body">${errorMessage}</div>
            </div>
        </div>
    </c:if>
    <div class="container">
        <h2>Cart</h2>
    </div>
    <div class="panel"></div>
    <div class="row">
        <div class="col-2"></div>

        <div class="col-8">
            <table class="table table-bordered text-center">
                <thead>
                <tr class="bg-light">
                    <td>
                        Brand
                    </td>
                    <td>
                        Model
                    </td>
                    <td>
                        Color
                    </td>
                    <td>
                        Display size
                    </td>
                    <td>
                        Quantity
                    </td>
                    <td>
                        Price
                    </td>
                </tr>
                </thead>
                <c:forEach var="item" items="${order.orderItems}">
                    <tr>
                        <td class="align-middle">
                                ${item.phone.brand}
                        </td>
                        <td class="align-middle">
                                ${item.phone.model}
                        </td>
                        <td class="align-middle">
                            <ul>
                                <c:forEach var="color" items="${item.phone.colors}">
                                    <li>${color.code}</li>
                                </c:forEach>
                            </ul>
                        </td>
                        <td class="align-middle">
                                ${item.phone.displaySizeInches}"
                        </td>
                        <td class="align-middle">
                                ${item.quantity}
                        </td>
                        <td class="align-middle">
                                ${item.phone.price}
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td class="border-white"></td><td class="border-white"></td><td class="border-white"></td><td class="border-white"></td>
                    <td>
                        Subtotal
                    </td>
                    <td>
                            ${order.subtotal}
                    </td>
                </tr>
                <tr>
                    <td class="border-white"></td><td class="border-white"></td><td class="border-white"></td><td class="border-white"></td>
                    <td>
                        Delivery
                    </td>
                    <td>
                            ${order.deliveryPrice}
                    </td>
                </tr>
                <tr>
                    <td class="border-white"></td><td class="border-white"></td><td class="border-white"></td><td class="border-white"></td>
                    <td>
                        TOTAL
                    </td>
                    <td>
                            ${order.totalPrice}
                    </td>
                </tr>
            </table>


            <frm:form method="post" modelAttribute="orderDto" action="${pageContext.servletContext.contextPath}/order">
                <table class="table-borderless">
                    <tr>
                        <td class="align-top">
                            First name*:
                        </td>
                        <td>
                            <frm:input path="firstName" placeholder="First name"/>
                            <p class="text-danger"><frm:errors path="firstName"/></p>
                        </td>
                    </tr>
                    <tr>
                        <td class="align-top">
                            Last name*:
                        </td>
                        <td>
                            <frm:input path="lastName" placeholder="Last name"/>
                            <p class="text-danger"><frm:errors path="lastName"/></p>
                        </td>
                    </tr>
                    <tr>
                        <td class="align-top">
                            Delivery address*:
                        </td>
                        <td>
                            <frm:input path="deliveryAddress" placeholder="Address"/>
                            <p class="text-danger"><frm:errors path="deliveryAddress"/></p>
                        </td>
                    </tr>
                    <tr>
                        <td class="align-top">
                            Contact phone*:
                        </td>
                        <td>
                            <frm:input path="contactPhoneNo" placeholder="+123456789012"/>
                            <p class="text-danger"><frm:errors path="contactPhoneNo"/></p>
                        </td>
                    </tr>
                </table>
                <frm:textarea path="additionalInformation" placeholder="Additional information"/>
                <br>
                <button class="btn btn-light" type="submit">Order</button>
            </frm:form>
        </div>

        <div class="col-2"></div>
    </div>
</tags:master>