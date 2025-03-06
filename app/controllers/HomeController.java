package controllers;

import models.Coffee;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.*;

import java.util.ArrayList;
import java.util.List;

public class HomeController extends Controller {
    public Result index(Http.Request request) {
        return ok(index.render(request)); // Removed coffeeList from index
    }

    public Result menu(Http.Request request) {
        List<Coffee> coffeeList = Coffee.find.all(); // Fetch coffee items
        return ok(menu.render(coffeeList, request)); // Pass coffeeList to menu
    }

    // ✅ Fetch only cart items stored in session
    public Result cart(Http.Request request) {
        List<Coffee> cartItems = getCartItemsFromSession(request); //  Get cart items properly
        return ok(views.html.cart.render(cartItems, request)); //  Ensure correct parameter order
    }



    public Result checkout(Http.Request request) {
        List<Coffee> cartItems = getCartItemsFromSession(request); //  Get cart items
        return ok(views.html.checkout.render(cartItems, request)); //  Pass correct order
    }

    // ✅ Get cart items stored in session
    private List<Coffee> getCartItemsFromSession(Http.Request request) {
        List<Coffee> cartItems = new ArrayList<>();

        // Retrieve cart from session (stored as a comma-separated string of coffee IDs)
        String cartData = request.session().getOptional("cart").orElse("");

        if (!cartData.isEmpty()) {
            String[] coffeeIds = cartData.split(","); // Split into an array of IDs

            for (String id : coffeeIds) {
                Coffee coffee = Coffee.find.byId(Integer.parseInt(id)); // ✅ Convert to Integer
                 // Fetch coffee from DB
                if (coffee != null) {
                    cartItems.add(coffee);
                }
            }
        }
        return cartItems;
    }

    // ✅ Add to Cart (Stores coffee ID in session)
    public Result addToCart(Http.Request request, Long coffeeId) {
        String cartData = request.session().getOptional("cart").orElse(""); // Get existing cart
        cartData = cartData.isEmpty() ? coffeeId.toString() : cartData + "," + coffeeId; // Append ID
        return redirect(routes.HomeController.cart()).addingToSession(request, "cart", cartData); // Store in session
    }

    // ✅ Remove from Cart (Removes coffee ID from session)
    public Result removeFromCart(Http.Request request, Integer id) {
        String cartData = request.session().getOptional("cart").orElse(""); // Get existing cart

        if (!cartData.isEmpty()) {
            String[] coffeeIds = cartData.split(",");
            List<String> updatedCart = new ArrayList<>();

            for (String coffeeIdStr : coffeeIds) {
                if (!coffeeIdStr.equals(id.toString())) { // Keep all except the one being removed
                    updatedCart.add(coffeeIdStr);
                }
            }

            String updatedCartData = String.join(",", updatedCart);
            return redirect(routes.HomeController.cart()).addingToSession(request, "cart", updatedCartData); // Update session
        }

        return redirect(routes.HomeController.cart()); // Redirect if cart is empty
    }
}
