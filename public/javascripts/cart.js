document.addEventListener("DOMContentLoaded", function () {
    // Event delegation for dynamically added items
    document.addEventListener("click", function (event) {
        // Handle "Add to Cart" button click
        if (event.target.classList.contains("add-to-cart")) {
            const coffeeId = event.target.getAttribute("data-id");  // Getting the coffee ID
            const coffeeName = event.target.getAttribute("data-name"); // Getting the coffee name
            const coffeePrice = event.target.getAttribute("data-price"); // Getting the coffee price

            // Check if any of the data attributes are missing
            if (!coffeeId || !coffeeName || !coffeePrice) {
                console.error("Missing coffee data attributes: coffeeId, coffeeName, or coffeePrice");
                return;  // Exit if necessary attributes are not found
            }

            // Add to cart request
            fetch(`/add-to-cart/${coffeeId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': document.querySelector('meta[name="csrf-token"]').getAttribute('content')
                },
                body: JSON.stringify({
                    name: coffeeName,
                    price: coffeePrice
                })
            })
            .then(response => response.json())
            .then(data => {
                if (data.status === "success") {
                    alert("Item added to cart!");
                    updateCartCount();
                } else {
                    alert("Error adding item to cart.");
                }
            })
            .catch(error => {
                console.error("Error:", error);
            });
        }

        // Handle "Edit Product" button click
        if (event.target.classList.contains("edit-product")) {
            let index = event.target.getAttribute("data-index");
            editItem(index);
        }
    });
});

// Function to update the cart count
function updateCartCount() {
    fetch("/cart-count")
        .then(response => response.json())
        .then(data => {
            document.getElementById("cart-count").innerText = data.count;
        })
        .catch(error => console.error("Error fetching cart count:", error));
}

// Function to edit product details
function editItem(index) {
    console.log("Editing item at index:", index);

    let menuItem = document.querySelectorAll(".menu-item")[index];
    if (!menuItem) {
        console.error("Menu item not found at index:", index);
        return;
    }

    let newName = prompt("Enter new name:", menuItem.querySelector(".item-name").innerText);
    let newDescription = prompt("Enter new description:", menuItem.querySelector(".item-description").innerText);
    let newPrice = prompt("Enter new price:", menuItem.querySelector(".item-price").innerText);

    if (!newName || !newDescription || !newPrice) {
        alert("Invalid input! Edit canceled.");
        return;
    }

    fetch("/update-item", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN": document.querySelector('meta[name="csrf-token"]').getAttribute("content")
        },
        body: JSON.stringify({
            index: index,
            name: newName,
            description: newDescription,
            price: newPrice
        })
    })
    .then(response => response.json())
    .then(data => {
        console.log("Server response:", data);
        if (data.status === "success") {
            alert("Item updated successfully!");
            menuItem.querySelector(".item-name").innerText = newName;
            menuItem.querySelector(".item-description").innerText = newDescription;
            menuItem.querySelector(".item-price").innerText = newPrice;
        } else {
            alert("Error updating item.");
        }
    })
    .catch(error => {
        console.error("Error:", error);
    });
}
