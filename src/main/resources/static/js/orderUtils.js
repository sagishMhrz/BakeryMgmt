initDropdowns();

function deleteOrder(orderId) {
    // Create and display a confirmation modal
    const modal = createModalMessage(
        "Warning",
        "You are deleting an order",
        "This operation cannot be undone. Click 'Ok' to confirm.",
        "warning"
    );

    // Add click event handler for the "Ok" button
    modal.querySelector("#okButton").onclick = function() {
        modal.remove(); // Remove the modal
        sendDeleteRequest(orderId); // Proceed with deletion
    };

    // Add click event handler for the "Cancel" button to close the modal
    modal.querySelector("#cancelButton").onclick = function() {
        modal.remove(); // Remove the modal when cancel is clicked
    };
}

function sendDeleteRequest(orderId) {
    // Create a new XMLHttpRequest object
    const request = new XMLHttpRequest();

    // Configure it: DELETE-request for the URL /api/order/{orderId}
    request.open("DELETE", `/api/order/${orderId}`, true);

    // Set up the callback for when the request completes
    request.onload = function () {
        let response;
        try {
            response = JSON.parse(this.responseText);
        } catch (e) {
            createModalMessage("Error", "Invalid response format", "The server responded with an unexpected format.", "error");
            return;
        }

        // Check the status of the request
        if (this.status === 200) {
            // Show success message and redirect
            const modal = createModalMessage(
                "Success",
                "Completed",
                response["response"],
                "success"
            );
            const okButton = modal.querySelector("#okButton");
            okButton.onclick = function () {
                modal.remove();
                window.location = "/orders"; // Redirect after confirmation
            }
        } else {
            // Handle HTTP error responses
            createModalMessage(
                "Error",
                "An error has occurred",
                response["response"] || "Unknown error",
                "error"
            );
        }
    };

    // Set up the callback for when the request fails
    request.onerror = function () {
        createModalMessage(
            "Error",
            "Network Error",
            "There was a problem with the network request.",
            "error"
        );
    };

    // Send the request
    request.send();
}

function createModalMessage(title, subtitle, message, type) {
    // Define TailwindCSS classes for different modal types with explicitly defined button colors
    const modalClasses = {
        success: "bg-green-50 border-l-4 border-green-500 text-green-800",
        error: "bg-red-50 border-l-4 border-red-500 text-red-800",
        warning: "bg-yellow-50 border-l-4 border-yellow-500 text-yellow-800"
    };

    // Define explicit color classes for the 'Ok' button based on type
    const buttonColors = {
        success: "bg-green-600 hover:bg-green-700",
        error: "bg-red-600 hover:bg-red-700",
        warning: "bg-yellow-600 hover:bg-yellow-700"
    };

    // Create modal HTML structure with updated explicit button colors
    const modalHtml = `
        <div class="fixed inset-0 flex items-center justify-center z-50 bg-gray-800 bg-opacity-50">
            <div class="bg-white border rounded-lg shadow-lg p-6 max-w-sm w-full ${modalClasses[type]}">
                <h2 class="text-xl font-bold mb-2">${title}</h2>
                <h3 class="text-lg mb-4">${subtitle}</h3>
                <p class="mb-4">${message}</p>
                <div class="flex justify-end space-x-4">
                    <button id="okButton" class="px-4 py-2 ${buttonColors[type]} text-white font-semibold rounded-lg transition duration-150">Ok</button>
                    <button id="cancelButton" class="px-4 py-2 bg-gray-300 text-gray-800 font-semibold rounded-lg hover:bg-gray-400 transition duration-150">Cancel</button>
                </div>
            </div>
        </div>
    `;

    // Insert the modal HTML into the document body
    const modalElement = document.createElement("div");
    modalElement.innerHTML = modalHtml;
    document.body.appendChild(modalElement);

    // Return the modal element for further manipulation
    return modalElement;
}



let itemList = new Array();

let cart;
let itemListElement;
let priceField;
let tableField;
let saveOrderButton;

initCartUtils();

function initCartUtils() {
     cart = document.getElementById('cart');
     itemListElement = document.getElementById("itemList");
     priceField = document.getElementById("cartPrice");
     tableField = document.getElementById("tableNumberField");

     saveOrderButton = document.getElementById("saveOrderButton");
     saveOrderButton.onclick = function () {
         saveOrder();
     }
}

function removeItemFromCart(item) {
    const index = itemList.indexOf(item);

    itemList.splice(index, 1);
}

function clearCart() {
    itemList.splice(0, itemList.length);
    itemListElement.innerHTML = "";
    priceField.innerText = '0.00';
}

function createCartItem(item) {
    const cartItem = document.createElement("div");
    cartItem.className = "cart-item";

    const itemId = document.createElement("span");
    itemId.className = "item-id";
    itemId.innerText = item["product"]["productId"];

    const itemName = document.createElement("span");
    itemName.className = "item-name";
    itemName.innerText = item["product"]["name"];

    const itemPrice = document.createElement("span");
    itemPrice.className = "item-price";
    itemPrice.innerText = item["product"]["price"];

    const removeButton = document.createElement("span");
    removeButton.className = "item-remove";
    removeButton.innerText = "X";
    removeButton.onclick = function (e) {
        if (e.target === removeButton) {
            cartItem.remove();
            removeItemFromCart(item);
            updateCartPrice(-1 * item["product"]["price"]);
        }
    }

    cartItem.appendChild(itemId);
    cartItem.appendChild(itemName);
    cartItem.appendChild(itemPrice)
    cartItem.appendChild(removeButton);

    itemListElement.appendChild(cartItem);

    updateCartPrice(item["product"]["price"]);
}

function updateCartPrice(price) {
    const currentPrice = parseFloat(priceField.innerText);

    priceField.innerText = (currentPrice + price).toFixed(2);
}

function saveOrder() {
    if(! Number.isFinite(parseInt(tableField.value)) || tableField.value === "") {
        createModalMessage("Error", "Invalid input", "Please enter a valid table number", "error");
        return;
    }

    if(! itemList.length > 0) {
        createModalMessage("Error", "Invalid input", "Please select at least 1 item to the cart.", "error");
        return;
    }

    const tableId = parseInt(tableField.value);

    const tableControl = new XMLHttpRequest(); // check for if a table is exists with given table id
    tableControl.open("get", "/api/table/"+tableId);

    tableControl.onload = function () {
        const response = JSON.parse(this.responseText);
        if(this.status === 200) {

            const orderObj = {
                items: itemList,
                totalPrice: parseFloat(priceField.innerText),
                table: response,
                orderDate: new Date()
            }

            sendSaveOrderRequest(orderObj);
        }
        else {
            createModalMessage("Error", "Transaction failed", response["response"], "error");
        }
    }
    tableControl.send();

}

function sendSaveOrderRequest(orderObj) {
    const request = new XMLHttpRequest();
    request.open("POST", "/api/order/save");
    request.onload = function () {
        const response = JSON.parse(this.responseText);

        if(this.status === 200) {
            const modal = createModalMessage("Success", "Transaction completed", response["response"], "success");
            modal.querySelector("#okButton").onclick = function () {
                modal.remove();
                window.location = window.location;
                clearCart();
            }
        }
        else {
            createModalMessage("Error", "Transaction failed", response["response"], "error");
        }
    }
    request.setRequestHeader("Content-Type", "application/json");
    request.send(JSON.stringify(orderObj));
}

function handleQuantities(itemObj) {
    for(let item of itemList) {
        if(item["product"]["productId"] === itemObj["product"]["productId"]) {
            item["quantity"] += 1;
            createCartItem(itemObj);
            return;
        }
    }
    itemList.push(itemObj);
    createCartItem(itemObj);
}

function addItemToCart(itemId) {
    const request = new XMLHttpRequest();
    request.open("GET", "/api/product/"+itemId);
    request.onload = function () {
        if(this.status === 200) {
            const product = JSON.parse(this.responseText);

            const itemObj = {
                product: product,
                quantity: 1
            }

            handleQuantities(itemObj);
        } else {
            createModalMessage("Error", "Connection Error", "Not able to add item to the cart. Please be sure that the server is online and your connection is ok.", "error");
        }
    }
    request.send();
}

function filterByDateInterval () {
    const interval = {
        startDate: new Date(),
        endDate: new Date()
    }

    interval.startDate = document.querySelector("#startDate").value;
    interval.endDate = document.querySelector("#endDate").value;

    window.location = "/orders/filter/interval"+"?"+"start="+interval.startDate+"&"+"end="+interval.endDate;
}

function filterByItem() {
    const name = document.querySelector("#itemName").innerText;
    window.location = "/orders/filter/item"+"?"+"name="+name;
}