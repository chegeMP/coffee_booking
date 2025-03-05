document.addEventListener("DOMContentLoaded", function () {
    console.log("cart.js loaded");

    let cart = JSON.parse(localStorage.getItem("cart")) || [];

    function updateCartUI() {
        const cartContainer = document.querySelector(".cart-items");
        const totalElement = document.querySelector(".cart-total");

        if (!cartContainer || !totalElement) return;

        cartContainer.innerHTML = "";
        let total = 0;

        cart.forEach((item, index) => {
            total += item.price * item.quantity;
            cartContainer.innerHTML += `
                <div class="cart-item">
                    <span>${item.name} x${item.quantity}</span>
                    <span>KES ${(item.price * item.quantity).toFixed(2)}</span>
                    <button class="remove-item" data-index="${index}">Remove</button>
                </div>`;
        });

        totalElement.textContent = `Total: KES ${total.toFixed(2)}`;
    }

    function addToCart(name, price) {
        console.log(`Adding to cart: ${name} - KES ${price}`);

        const existingItem = cart.find(item => item.name === name);
        if (existingItem) {
            existingItem.quantity += 1;
        } else {
            cart.push({ name, price, quantity: 1 });
        }
        localStorage.setItem("cart", JSON.stringify(cart));
        updateCartUI();
    }

    document.querySelectorAll(".add-to-cart").forEach(button => {
        console.log("Button found:", button);
        button.addEventListener("click", function () {
            const name = this.getAttribute("data-name");
            const price = parseFloat(this.getAttribute("data-price"));

            console.log(`Button clicked: ${name} - KES ${price}`);
            addToCart(name, price);
        });
    });

    document.addEventListener("click", function (e) {
        if (e.target.classList.contains("remove-item")) {
            const index = e.target.getAttribute("data-index");
            cart.splice(index, 1);
            localStorage.setItem("cart", JSON.stringify(cart));
            updateCartUI();
        }
    });

    updateCartUI();
});
