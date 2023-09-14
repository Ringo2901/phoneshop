<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="pageTitle" required="true" %>

<html>
<head>
    <title>${pageTitle}</title>
    <script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" integrity="sha384-lW5x5bFPd5R5J1GPpVgH5m2F5GfHJqUpktz5q5l5F5CkC4x5Kf5T5b5T5q5l5X5j5f5l5" crossorigin="anonymous">

    <script src="${pageContext.servletContext.contextPath}/resources/js.addToCart.js"></script>
</head>
<body>
<header>
    <div class="container bg-dark">
        <div class="row">
            <div class="col-6">
                <h1>
                    <a style="font-family: 'Lobster'" class="text-light" href="${pageContext.servletContext.contextPath}">
                        Phonify
                    </a>
                </h1>
            </div>
            <div class="col-6">
                <div class="float-right">
                    <button class="btn btn-light"> My Cart:
                        <span id="cartTotalQuantity"><c:out value="${cart.totalQuantity}"/></span> items
                        <span id="cartTotalCost"><c:out value="${cart.totalCost}"/></span>$
                    </button>
                </div>
            </div>
        </div>
    </div>
</header>
<main>
    <jsp:doBody/>
</main>
</body>
</html>