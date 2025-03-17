package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Coffee;
import models.CoffeeSize;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;
import play.libs.Files;
import play.libs.Files.TemporaryFile;

import javax.inject.Inject;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import views.html.admin.dashboard;

public class AdminController extends Controller {

    private final FormFactory formFactory;

    @Inject
    public AdminController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    // Admin Panel Dashboard
    public Result dashboard(Http.Request request) {
        return ok(dashboard.render("Admin Dashboard"));
    }

    // List all coffee items
    public Result list() {
        List<Coffee> coffees = Coffee.find.all();
        return ok(views.html.admin.coffee_list.render(coffees));
    }

    // Show form to create a new coffee
    public Result createForm(Http.Request request) {
        Form<Coffee> coffeeForm = formFactory.form(Coffee.class);
        return ok(views.html.admin.add_coffee.render(coffeeForm, request));
    }

    // Handle coffee creation
    public Result create(Http.Request request) {
        Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
        TemporaryFile tempFile = null;
        String fileName = null;

        if (body != null) {
            Http.MultipartFormData.FilePart<TemporaryFile> imageFile = body.getFile("image");
            if (imageFile != null) {
                fileName = System.currentTimeMillis() + "_" + imageFile.getFilename();
                tempFile = imageFile.getRef();
            }
        }

        Form<Coffee> coffeeForm = formFactory.form(Coffee.class).bindFromRequest(request);

        if (coffeeForm.hasErrors()) {
            return badRequest(views.html.admin.add_coffee.render(coffeeForm, request))
                    .flashing("error", "Invalid input. Please try again.");
        }

        Coffee coffee = coffeeForm.get();

        // ✅ Save the image if provided
        if (tempFile != null) {
            String uploadPath = "public/uploads/";
            File destFile = new File(uploadPath + fileName);
            try {
                java.nio.file.Files.createDirectories(Path.of(uploadPath));
                tempFile.moveFileTo(destFile, true);
                coffee.setImageUrl("/assets/uploads/" + fileName); // Use setter method here
            } catch (Exception e) {
                return internalServerError("File upload failed: " + e.getMessage());
            }
        }

        coffee.save();
        return redirect(routes.AdminController.list()).flashing("success", "Coffee added successfully!");
    }

    // Delete a coffee item
    public Result delete(Http.Request request, int id) {
        Coffee coffee = Coffee.find.byId(id);
        if (coffee != null) {
            coffee.delete();
        }
        return redirect(routes.AdminController.list()).flashing("success", "Coffee deleted!");
    }

    // Show form to edit an existing coffee item
    public Result editForm(Http.Request request, int id) {
        Coffee coffee = Coffee.find.byId(id);
        if (coffee == null) {
            return redirect(routes.AdminController.list())
                    .flashing("error", "Coffee item not found!");
        }
        Form<Coffee> coffeeForm = formFactory.form(Coffee.class).fill(coffee);
        return ok(views.html.admin.edit_coffee.render(coffeeForm, coffee, request));
    }

    // Handle coffee update
    public Result update(Http.Request request, int id) {
        Coffee existingCoffee = Coffee.find.byId(id);
        if (existingCoffee == null) {
            return redirect(routes.AdminController.list())
                    .flashing("error", "Coffee item not found!");
        }

        Form<Coffee> coffeeForm = formFactory.form(Coffee.class).bindFromRequest(request);
        if (coffeeForm.hasErrors()) {
            return badRequest(views.html.admin.edit_coffee.render(coffeeForm, existingCoffee, request))
                    .flashing("error", "Invalid form submission. Please check your inputs.");
        }

        Coffee updatedCoffee = coffeeForm.get();

        // ✅ Handle image file upload (if provided)
        Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
        TemporaryFile tempFile = null;
        String fileName = null;

        if (body != null) {
            Http.MultipartFormData.FilePart<TemporaryFile> imageFile = body.getFile("image");
            if (imageFile != null) {
                fileName = System.currentTimeMillis() + "_" + imageFile.getFilename();
                tempFile = imageFile.getRef();
            }
        }

        if (tempFile != null) {
            String uploadPath = "public/uploads/";
            File destFile = new File(uploadPath + fileName);
            try {
                java.nio.file.Files.createDirectories(Path.of(uploadPath));
                tempFile.moveFileTo(destFile, true);
                existingCoffee.setImageUrl("/assets/uploads/" + fileName); // Use setter method here
            } catch (Exception e) {
                return badRequest(views.html.admin.edit_coffee.render(coffeeForm, existingCoffee, request))
                        .flashing("error", "Image upload failed: " + e.getMessage());
            }
        }

        // ✅ Update other fields using setter methods
        existingCoffee.setName(updatedCoffee.getName());
        existingCoffee.setDescription(updatedCoffee.getDescription());
        existingCoffee.setPrice(new BigDecimal(updatedCoffee.getPrice()));  // Ensure correct BigDecimal conversion
        existingCoffee.setSize(updatedCoffee.getSize());
        existingCoffee.setQuantity(updatedCoffee.getQuantity());

        existingCoffee.update(); // Save updated coffee object

        return redirect(routes.AdminController.list())
                .flashing("success", "Coffee updated successfully!");
    }

    // ✅ NEW: Handle AJAX-based update for coffee item
    public Result updateItem(Http.Request request) {
        JsonNode json = request.body().asJson();
        if (json == null) {
            return badRequest("Invalid JSON data");
        }

        int id = json.findPath("id").asInt();
        String name = json.findPath("name").asText();
        String description = json.findPath("description").asText();
        String priceText = json.findPath("price").asText();
        String sizeString = json.findPath("size").asText();
        int quantity = json.findPath("quantity").asInt();
        String imageUrl = json.findPath("imageUrl").asText();

        if (name == null || description == null || priceText == null || sizeString == null) {
            return badRequest("Missing required fields");
        }

        BigDecimal price;
        try {
            price = new BigDecimal(priceText);  // Proper BigDecimal parsing
        } catch (NumberFormatException e) {
            return badRequest("Invalid price format");
        }

        Coffee coffee = Coffee.find.byId(id);
        if (coffee == null) {
            return notFound("Coffee item not found");
        }

        try {
            coffee.setName(name);  // Use setter instead of direct field access
            coffee.setDescription(description);
            coffee.setPrice(price);
            coffee.setSize(CoffeeSize.valueOf(sizeString.toUpperCase()));
            coffee.setQuantity(quantity);
            coffee.setImageUrl(imageUrl);  // Use setter method for imageUrl
            coffee.update();
        } catch (IllegalArgumentException e) {
            return badRequest("Invalid size value");
        }

        return ok("Item updated successfully");
    }
}
