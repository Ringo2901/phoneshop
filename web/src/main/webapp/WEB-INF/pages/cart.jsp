<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="frm" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Cart">
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
        <div class="panel-heading">Success</div>
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
      <c:if test="${cart.items.size() > 0}">
      <frm:form method="put" modelAttribute="cartItemsQuantities" action="${pageContext.servletContext.contextPath}/cart">
      <table class="table table-hover table-bordered text-center">
        <thead>
          <tr class="bg-light">
            <td>
              Brand
            </td>
            <td>
              Model
            </td>
            <td>Color</td>
            <td>
              Display size
            </td>
            <td>
              Price
            </td>
            <td>Quantity</td>
            <td>Action</td>
          </tr>
        </thead>
        <c:forEach var="i" begin="0" end="${cart.items.size() - 1}">
          <tr id="row${cart.items[i].phone.id}">
            <td class="align-middle">
              <a href="<c:url value="/productDetails/${cart.items[i].phone.id}"/>">${cart.items[i].phone.brand}</a>
            </td>
            <td class="align-middle">
              <a href="<c:url value="/productDetails/${cart.items[i].phone.id}"/>">${cart.items[i].phone.model}</a>
            </td>
            <td class="align-middle">
              <ul>
                <c:forEach var="color" items="${cart.items[i].phone.colors}">
                  <li>${color.code}</li>
                </c:forEach>
              </ul>
            </td>
            <td class="align-middle">${cart.items[i].phone.displaySizeInches}"</td>
            <td class="align-middle">$ ${cart.items[i].phone.price}</td>
            <td class="align-middle">
              <frm:input path="items[${i}].quantity"/>
              <p class="text-danger">
                <frm:errors path="items[${i}].quantity"/>
                <frm:errors path="items[${i}]"/>
              </p>
              <frm:hidden path="items[${i}].phoneId"/>
              <p class="text-danger" id="statusMessage${cart.items[i].phone.id}"></p>
            </td>
            <td class="align-middle">
              <button form="deleteCartItem" class="btn btn-outline-dark border-dark"
              formaction="<c:url value="/cart?phoneId=${cart.items[i].phone.id}"/>">Delete</button>
            </td>
          </tr>
        </c:forEach>
      </table>
        <p>
          <button class="btn btn-lg btn-outline-light text-dark border-dark float-right" type="submit">Update</button>
        </p>
      </frm:form>
        <form id="deleteCartItem" method="post">
          <input type="hidden" name="_method" value="delete">
        </form>
      </c:if>
    </div>

    <div class="col-2"></div>
  </div>
</tags:master>