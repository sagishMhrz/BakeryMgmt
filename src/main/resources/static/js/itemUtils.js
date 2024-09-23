
/* DELETE ITEM */
function deleteItem(itemId) {
    const modal = createModalMessage("Warning","Item with id:"+itemId+" is deleting", "To confirm, please click 'Ok'.","warning");
    modal.querySelector("#okButton").onclick = function () {
        modal.remove();
        sendDeleteRequest(itemId);
    }
}

function sendDeleteRequest(itemId) {
    const url = "/api/product/"+itemId;

    function showResult(response) {
        const modal = createModalMessage("Success","Transaction completed", response["response"],"success");
        modal.querySelector("#okButton").onclick = function () {
            modal.remove();
            window.location = "/products";
        }
    }

    createRequest("delete", url, showResult)

}

/* - */

/* SAVE ITEM */
// function saveItemFromFormData() {
//     const form = document.querySelector("#itemForm");
//     const data = new FormData(form);
//
//     const itemObj = {
//         name: "",
//         description: "",
//         type: "",
//         price: 0,
//         stock: 0,
//         isStockRequired: false
//     }
//
//     itemObj.name = data.get("name");
//     itemObj.description = data.get("description");
//     itemObj.type = data.get("type");
//     itemObj.price = data.get("price");
//     itemObj.stock = data.get("stock");
//     itemObj.isStockRequired = document.getElementById("isStockRequired").checked;
//
//     console.log(itemObj);
//
//     const url = "/api/product/save";
//
//     function showResult(response) {
//         const modal = createModalMessage("Success", "Transaction completed", response["response"], "success");
//         modal.querySelector("#okButton").onclick = function () {
//             modal.remove();
//             //window.location = "/products";
//         }
//     }
//     createRequest("post", url, showResult, JSON.stringify(itemObj));
// }
function saveItemFromFormData() {
    const form = document.querySelector("#itemForm");
    const data = new FormData(form);

    const url = "/api/product/save";

    function showResult(response) {
        const modal = createModalMessage("Success", "Transaction completed", response["response"], "success");
        modal.querySelector("#okButton").onclick = function () {
            modal.remove();
            //window.location = "/products";
        }
    }

    const xhr = new XMLHttpRequest();
    xhr.open("POST", url, true);
    xhr.onload = function () {
        if (xhr.status === 200) {
            showResult(JSON.parse(xhr.responseText));
        } else {
            alert("Failed to upload item: " + xhr.responseText);
        }
    };
    xhr.send(data);
}

/* - */

/* UPDATE STOCK */
function createStockUpdatePopup(itemId) {
    // Update the popup body with TailwindCSS classes and better visuals
    const body = `
        <div class="flex flex-col gap-6 p-6 bg-white shadow-lg rounded-xl max-w-lg mx-auto border border-gray-200">
            <label for="amountField" class="text-xl font-bold text-gray-900">Amount:</label>
            <input id="amountField" type="number" min="1" 
                class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-4 focus:ring-[#F0D7A7] placeholder-gray-500 text-lg"
                placeholder="Enter stock amount">
        </div>
    `;

    // Create the modal with TailwindCSS styles and enhanced title
    const modal = createModalFromBody(body, `
        <span class="text-2xl font-extrabold text-[#8D4821]">Add Stock for Item ${itemId}</span>
        <button class="text-gray-400 hover:text-gray-600 transition duration-200 absolute top-4 right-4" onclick="modal.remove()">×</button>
    `);

    // Add TailwindCSS classes to buttons for enhanced visuals
    const okButton = modal.querySelector("#okButton");
    const cancelButton = modal.querySelector("#cancelButton");

    okButton.classList.add(
        "bg-[#8D4821]", "text-white", "py-3", "px-6", "rounded-lg",
        "hover:bg-[#733517]", "transition", "duration-300", "font-semibold", "shadow-md",
        "w-full", "sm:w-auto", "text-center", "flex-1", "text-lg"
    );

    cancelButton.classList.add(
        "bg-gray-500", "text-white", "py-3", "px-6", "rounded-lg",
        "hover:bg-gray-600", "transition", "duration-300", "font-semibold", "shadow-md",
        "w-full", "sm:w-auto", "text-center", "flex-1", "text-lg"
    );

    // Create a container to hold both buttons with proper alignment and spacing
    const buttonContainer = document.createElement("div");
    buttonContainer.classList.add("flex", "justify-between", "mt-8", "gap-4");

    buttonContainer.appendChild(okButton);
    buttonContainer.appendChild(cancelButton);

    // Append the buttons container to the modal body
    modal.querySelector('.modal-body').appendChild(buttonContainer);

    // Button click handlers
    okButton.onclick = function () {
        const amount = parseInt(modal.querySelector("#amountField").value);
        if (!isNaN(amount) && amount > 0) {
            sendUpdateStockRequest(itemId, amount);
            modal.remove();
        } else {
            alert("Please enter a valid stock amount.");
        }
    };

    cancelButton.onclick = function () {
        modal.remove();
    };
}


function sendUpdateStockRequest(itemId, amount) {
    const params = "?amount=" + amount;
    const url = "/api/product/" + itemId + "/stock" + params;

    function showResult(response) {
        // Create the success message modal
        const modal = createModalMessage(
            "Success",
            `<div class="flex items-center gap-4">
                <svg class="w-8 h-8 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                </svg>
                <span>Stock updated successfully</span>
            </div>`,
            response["response"],
            "success"
        );

        // Style the "OK" button
        const okButton = modal.querySelector("#okButton");
        okButton.classList.add(
            "bg-green-600", "text-white", "py-2", "px-6", "rounded-lg",
            "hover:bg-green-700", "transition", "duration-300", "font-semibold", "shadow-md", "mt-4"
        );

        // Optional: Add animation to the modal
        modal.classList.add("animate-fade-in");
    }

    createRequest("get", url, showResult);
}

// Add keyframe animation using Tailwind's `@keyframes`
const styles = document.createElement('style');
styles.innerHTML = `
    @keyframes fade-in {
        from { opacity: 0; transform: translateY(-10px); }
        to { opacity: 1; transform: translateY(0); }
    }
    .animate-fade-in {
        animation: fade-in 0.3s ease-out;
    }
`;
document.head.appendChild(styles);


/* - */

/* UPDATE PRICE */
function updatePrice(itemId) {
    // Update the body with proper TailwindCSS classes for better visual appeal
    const body = `
        <div class="flex flex-col gap-6 p-6 bg-white shadow-lg rounded-xl max-w-md mx-auto border border-gray-200">
            <label for="priceField" class="text-xl font-bold text-gray-900">New Price:</label>
            <input id="priceField" type="number" min="1" step="0.01" 
                class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-gray-400"
                placeholder="Enter new price">
        </div>
    `;

    // Create the modal
    const modal = createModalFromBody(body, `<span class="text-2xl font-bold text-[#8D4821]">Update Price</span>`);

    // Style buttons for enhanced visuals
    const okButton = modal.querySelector("#okButton");
    const cancelButton = modal.querySelector("#cancelButton");

    okButton.classList.add(
        "bg-[#8D4821]", "text-white", "py-3", "px-6", "rounded-lg",
        "hover:bg-[#733517]", "transition", "duration-300", "font-semibold", "shadow-md",
        "w-full", "sm:w-auto", "text-center", "flex-1", "text-lg"
    );

    cancelButton.classList.add(
        "bg-gray-500", "text-white", "py-3", "px-6", "rounded-lg",
        "hover:bg-gray-600", "transition", "duration-300", "font-semibold", "shadow-md",
        "w-full", "sm:w-auto", "text-center", "flex-1", "text-lg"
    );

    // Create a button container for proper alignment
    const buttonContainer = document.createElement("div");
    buttonContainer.classList.add("flex", "justify-between", "mt-8", "gap-4");

    buttonContainer.appendChild(okButton);
    buttonContainer.appendChild(cancelButton);

    // Append the button container to the modal body
    modal.querySelector('.modal-body').appendChild(buttonContainer);

    // Handle the "OK" button click
    okButton.onclick = function () {
        const newPrice = parseFloat(modal.querySelector("#priceField").value);
        if (!isNaN(newPrice) && newPrice > 0) {
            modal.remove();
            sendUpdatePriceRequest(itemId, newPrice);
        } else {
            alert("Please enter a valid price.");
        }
    };

    // Handle the "Cancel" button click
    cancelButton.onclick = function () {
        modal.remove();
    };
}

function sendUpdatePriceRequest(itemId, newPrice) {
    const url = "/api/product/" + itemId + "/price";

    function showResult(response) {
        // Create the success message modal
        const modal = createModalMessage(
            "Success",
            `<div class="flex items-center gap-4">
                <svg class="w-8 h-8 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                </svg>
                <span>Price updated successfully</span>
            </div>`,
            response["response"],
            "success"
        );

        const okButton = modal.querySelector("#okButton");
        okButton.classList.add(
            "bg-green-600", "text-white", "py-2", "px-6", "rounded-lg",
            "hover:bg-green-700", "transition", "duration-300", "font-semibold", "shadow-md"
        );
    }

    createRequest("post", url, showResult, newPrice);
}

/* - */

/* UPDATE DESCRIPTION */
function updateDescription(itemId) {
    const body = '' +
        '<label>New Description:<input id="descriptionField" type="text" minlength="1"></label>';

    const modal = createModalFromBody(body, "Update Description");
    modal.querySelector("#okButton").onclick = function () {
        const description = modal.querySelector("#descriptionField").value;
        modal.remove();
        sendUpdateDescriptionRequest(itemId, description);
    }
}

function sendUpdateDescriptionRequest(itemId, description) {
    const url = "/api/product/"+itemId+"/description";

    function showResult(response) {
        const modal = createModalMessage("Success", "Transaction completed", response["response"], "success");
    }

    createRequest("post", url, showResult, description);
}
/* - */

function createRequest(method = "get", url, onloadFunction, body = null) {
    const request = new XMLHttpRequest();
    request.open(method, url);
    request.onload = function () {
        const response = JSON.parse(this.responseText);
        if(this.status === 200) {
            onloadFunction(response);
        }
        else if(this.status === 403) {
            window.location = "/unauthorized";
        }
        else {
            createModalMessage("Error", "Transaction failed", response["response"], "error");
        }
    };

    if(body !== null) {
        request.setRequestHeader("Content-Type", "application/json");
        request.send(body);
    }
    else request.send();
}

