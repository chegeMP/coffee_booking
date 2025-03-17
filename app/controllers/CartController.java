package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartController extends Controller {
    private static final List<Map<String, Object>> cartItems = new ArrayList<>();
    private static final List<Map<String, Object>> menuItems = new ArrayList<>();

    // Add to Cart API
    public Result addToCart(Http.Request request) {
        JsonNode json = request.body().asJson();
        if (json == null || !json.has("name") || !json.has("price")) {
            return badRequest(Json.toJson(Map.of("status", "error", "message", "Invalid input")));
        }

        String name = json.get("name").asText();
        double price = json.get("price").asDouble();

        cartItems.add(Map.of("name", name, "price", price));
        return ok(Json.toJson(Map.of("status", "success")));
    }

    // Get Cart Count API
    public Result getCartCount() {
        return ok(Json.toJson(Map.of("count", cartItems.size())));
    }

    // Update Product API
    public Result updateItem(Http.Request request) {
        JsonNode json = request.body().asJson();
        if (json == null || !json.has("index") || !json.has("name") || !json.has("description") || !json.has("price")) {
            return badRequest(Json.toJson(Map.of("status", "error", "message", "Invalid input")));
        }

        int index = json.get("index").asInt();
        if (index < 0 || index >= menuItems.size()) {
            return badRequest(Json.toJson(Map.of("status", "error", "message", "Invalid item index")));
        }

        menuItems.set(index, Map.of(
                "name", json.get("name").asText(),
                "description", json.get("description").asText(),
                "price", json.get("price").asDouble()
        ));

        return ok(Json.toJson(Map.of("status", "success")));
    }
}
