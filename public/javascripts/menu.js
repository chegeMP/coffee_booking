document.addEventListener("DOMContentLoaded", function () {
    const menuGrid = document.getElementById("menu-grid");
    const addProductBtn = document.querySelector(".add-product-btn");
    const modal = document.getElementById("addProductModal");
    const closeModal = document.querySelector(".close");
    const saveProductBtn = document.querySelector(".save-product");

    let products = JSON.parse(localStorage.getItem("products")) || [];

    function renderMenu() {
        menuGrid.innerHTML = "";
        products.forEach((product, index) => {
            const item = document.createElement("div");
            item.classList.add("menu-item");
            item.innerHTML = `
                <img src="@routes.Assets.versioned("${product.img}")" alt="${product.name}" class="menu-img">
                <div class="menu-details">
                    <h3>${product.name}</h3>
                    <p>${product.desc}</p>
                    <span class="price">KES ${product.price}</span>
                    <button class="btn add-to-cart" data-name="${product.name}" data-price="${product.price}">Add to Cart</button>
                    <button class="btn remove-product" data-index="${index}">Remove</button>
                </div>
            `;
            menuGrid.appendChild(item);
        });
        attachRemoveEvent();
    }

    function attachRemoveEvent() {
        document.querySelectorAll(".remove-product").forEach(button => {
            button.addEventListener("click", function () {
                const index = this.getAttribute("data-index");
                products.splice(index, 1);
                localStorage.setItem("products", JSON.stringify(products));
                renderMenu();
            });
        });
    }

    addProductBtn.addEventListener("click", () => modal.style.display = "block");
    closeModal.addEventListener("click", () => modal.style.display = "none");

    saveProductBtn.addEventListener("click", function () {
        const name = document.getElementById("productName").value;
        const desc = document.getElementById("productDescription").value;
        const price = document.getElementById("productPrice").value;
        const img = document.getElementById("productImage").value;

        if (name && desc && price && img) {
            products.push({ name, desc, price, img });
            localStorage.setItem("products", JSON.stringify(products));
            renderMenu();
            modal.style.display = "none";
        } else {
            alert("Please fill all fields.");
        }
    });

    renderMenu();
});
