document.addEventListener("DOMContentLoaded", function () {
    console.log("📦 Checkout page loaded!");

    let cart = JSON.parse(localStorage.getItem("cart")) || [];
    const checkoutContainer = document.getElementById("checkout-items");
    const totalElement = document.getElementById("checkout-total");
    const placeOrderBtn = document.querySelector(".place-order-btn");

    function updateCheckoutUI() {
        console.log("🔄 Updating checkout UI...");

        if (!checkoutContainer || !totalElement) {
            console.error("❌ Checkout elements not found!");
            return;
        }

        checkoutContainer.innerHTML = "";
        let total = 0;

        cart.forEach(item => {
            let itemTotal = item.price * item.quantity;
            total += itemTotal;

            checkoutContainer.innerHTML += `
                <tr>
                    <td>${item.name}</td>
                    <td>KES ${item.price.toFixed(2)}</td>
                    <td>${item.quantity}</td>
                    <td>KES ${itemTotal.toFixed(2)}</td>
                </tr>`;
        });

        totalElement.textContent = total.toFixed(2);
        console.log("✅ Checkout UI updated!");
    }

    placeOrderBtn.addEventListener("click", function () {
        const name = document.getElementById("name").value.trim();
        const email = document.getElementById("email").value.trim();
        const phone = document.getElementById("phone").value.trim();
        const paymentMethod = document.querySelector('input[name="payment"]:checked').value;

        if (!name || !email || !phone) {
            alert("❌ Please fill in all billing details!");
            return;
        }

        console.log("🚀 Placing order...");
        console.log(`🧑 Name: ${name}`);
        console.log(`📧 Email: ${email}`);
        console.log(`📞 Phone: ${phone}`);
        console.log(`💳 Payment Method: ${paymentMethod}`);
        console.log(`🛍️ Order Total: KES ${totalElement.textContent}`);

        alert("✅ Order placed successfully! 🎉");

        localStorage.removeItem("cart"); // Clear cart after placing order
        window.location.href = "/"; // Redirect to home page after order
    });

    updateCheckoutUI();
});
