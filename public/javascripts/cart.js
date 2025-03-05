document.addEventListener("DOMContentLoaded", function () {
    let menuItems = document.querySelectorAll(".menu-item");

    // Function to enable editing
    function enableEditing() {
        document.querySelectorAll(".menu-item").forEach((item, index) => {
            let editBtn = document.createElement("button");
            editBtn.textContent = "Edit";
            editBtn.classList.add("btn", "edit-item");
            editBtn.setAttribute("data-index", index);
            item.querySelector(".menu-details").appendChild(editBtn);

            let removeBtn = document.createElement("button");
            removeBtn.textContent = "Remove";
            removeBtn.classList.add("btn", "remove-item");
            removeBtn.setAttribute("data-index", index);
            item.querySelector(".menu-details").appendChild(removeBtn);
        });

        attachEventListeners();
    }

    // Function to attach event listeners to edit and remove buttons
    function attachEventListeners() {
        document.querySelectorAll(".edit-item").forEach(button => {
            button.addEventListener("click", function () {
                const index = parseInt(this.getAttribute("data-index"));
                editItem(index);
            });
        });

        document.querySelectorAll(".remove-item").forEach(button => {
            button.addEventListener("click", function () {
                const index = parseInt(this.getAttribute("data-index"));
                removeItem(index);
            });
        });
    }

    // Function to edit a menu item
    function editItem(index) {
        let menuItem = document.querySelectorAll(".menu-item")[index];
        let name = menuItem.querySelector("h3");
        let description = menuItem.querySelector("p");
        let price = menuItem.querySelector(".price");

        let newName = prompt("Enter new name:", name.textContent);
        let newDescription = prompt("Enter new description:", description.textContent);
        let newPrice = prompt("Enter new price:", price.textContent.replace("KES ", ""));

        if (newName && newDescription && newPrice) {
            name.textContent = newName;
            description.textContent = newDescription;
            price.textContent = "KES " + newPrice;
        }
    }

    // Function to remove a menu item
    function removeItem(index) {
        if (confirm("Are you sure you want to remove this item?")) {
            let menuItem = document.querySelectorAll(".menu-item")[index];
            menuItem.remove();
        }
    }

    // Add item function
    document.getElementById("add-item-form").addEventListener("submit", function (event) {
        event.preventDefault();
        const name = document.getElementById("item-name").value;
        const description = document.getElementById("item-description").value;
        const price = document.getElementById("item-price").value;
        const image = document.getElementById("item-image").value;

        let newItem = document.createElement("div");
        newItem.classList.add("menu-item");
        newItem.innerHTML = `
            <img src="${image}" alt="${name}" class="menu-img">
            <div class="menu-details">
                <h3>${name}</h3>
                <p>${description}</p>
                <span class="price">KES ${price}</span>
                <button class="btn add-to-cart" data-name="${name}" data-price="${price}">Add to Cart</button>
            </div>
        `;

        document.querySelector(".menu-grid").appendChild(newItem);
        enableEditing();
        this.reset();
    });

    enableEditing();
});
