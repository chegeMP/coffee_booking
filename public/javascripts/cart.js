document.addEventListener("DOMContentLoaded", function () {
    // Grab CSRF Token once DOM is ready
    const csrfToken = document.querySelector('meta[name="csrf-token"]').getAttribute('content');

    // Event delegation for dynamically added items
    document.addEventListener("click", function (event) {
        // ✅ Add to Cart
        if (event.target.classList.contains("add-to-cart")) {
            const coffeeId = event.target.getAttribute("data-id");
            const coffeeName = event.target.getAttribute("data-name");
            const coffeePrice = event.target.getAttribute("data-price");

            if (!coffeeId || !coffeeName || !coffeePrice) {
                console.error("Missing coffee data attributes");
                return;
            }

            fetch(`/add-to-cart/${coffeeId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Csrf-Token': csrfToken
                },
                body: JSON.stringify({ name: coffeeName, price: coffeePrice })
            })
            .then(response => {
                if (!response.ok) throw new Error("Network response was not ok");
                return response.json();
            })
            .then(data => {
                if (data.status === "success") {
                    alert("Item added to cart!");
                    updateCartCount();
                } else {
                    alert("Error adding item to cart.");
                }
            })
            .catch(error => console.error("Error:", error));
        }

        // ✅ Edit Product
        if (event.target.classList.contains("edit-product")) {
            let index = event.target.getAttribute("data-index");
            editItem(index, csrfToken);
        }

        // ✅ Remove from Cart
        if (event.target.classList.contains("remove-from-cart")) {
            let itemId = event.target.getAttribute("data-id");
            if (!itemId) {
                console.error("Missing item ID for removal");
                return;
            }

            fetch(`/cart/remove/${itemId}`, {
                method: 'POST', // 🔥🔥 Important fix: POST method
                headers: {
                    'Content-Type': 'application/json',
                    'Csrf-Token': csrfToken
                }
            })
            .then(response => {
                if (!response.ok) throw new Error("Failed to remove item");
                return response.json();
            })
            .then(data => {
                if (data.status === "success") {
                    alert("Item removed from cart!");
                    updateCartCount(); // Optional, refresh the count
                    // Optionally remove item from DOM
                    event.target.closest(".cart-item").remove();
                } else {
                    alert("Failed to remove item");
                }
            })
            .catch(error => console.error("Error:", error));
        }
    });
});

// ✅ Update Cart Count
function updateCartCount() {
    fetch("/cart-count")
        .then(response => {
            if (!response.ok) throw new Error("Failed to fetch cart count");
            return response.json();
        })
        .then(data => {
            document.getElementById("cart-count").innerText = data.count;
        })
        .catch(error => console.error("Error fetching cart count:", error));
}

// ✅ Edit Product Function
function editItem(index, csrfToken) {
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
            "Csrf-Token": csrfToken
        },
        body: JSON.stringify({
            index: index,
            name: newName,
            description: newDescription,
            price: newPrice
        })
    })
    .then(response => {
        if (!response.ok) throw new Error("Failed to update item");
        return response.json();
    })
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
    .catch(error => console.error("Error:", error));
}
