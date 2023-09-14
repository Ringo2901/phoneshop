function addToCart(phoneId, quantity) {
    const quant = document.querySelector("#quantity" + phoneId);
    const status = document.querySelector("#statusMessage" + phoneId);
    const message = document.createElement('div');
    const messageHead = document.createElement('div');
    const messageBody = document.createElement('div');
    $.ajax({
        type: "POST",
        url: "ajaxCart",
        data: JSON.stringify({phoneId, quantity: quant.value}),
        dataType: "json",
        contentType: "application/json",
        success: function (data) {
            if (data.errorStatus == false) {
                message.style.color = "green"
                message.className = "panel panel-success";
                messageHead.innerText = "Success";
                status.innerHTML = "";
            } else {
                message.style.color = "red"
                message.className = "panel panel-danger";
                messageHead.innerText = "Error";
                status.innerHTML = data.message;
            }
            messageHead.className = "panel-heading";
            messageBody.className = "panel-body";
            messageBody.innerText = data.message;
            $("#cartTotalQuantity").text(data.totalQuantity);
            $("#cartTotalCost").text(data.totalCost);
        },
        error: function (data) {
            message.style.color = "red"
            message.className = "panel panel-danger";
            messageHead.innerText = "Error";
            status.innerHTML = "There was an error";
            messageHead.className = "panel-heading";
            messageBody.className = "panel-body";
            messageBody.innerText = "There was an error";
        }
    });
    message.append(messageHead, messageBody);
    document.querySelector('#statusMessage').replaceChildren(message);
}