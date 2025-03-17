package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Coffee;
import models.CoffeeSize;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;
import play.libs.Files.TemporaryFile;

import javax.inject.Inject;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
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
        Http.MultipartFormData.FilePart<TemporaryFile> imageFile = body.getFile("image");

        Form<Coffee> coffeeForm = formFactory.form(Coffee.class).bindFromRequest(request);

        if (coffeeForm.hasErrors()) {
            return badRequest(views.html.admin.add_coffee.render(coffeeForm, request));
        }

        Coffee coffee = coffeeForm.get();

        if (imageFile != null) {
            String fileName = imageFile.getFilename();
            TemporaryFile tempFile = imageFile.getRef();
            String uploadPath = "public/uploads/";
            File destFile = new File(uploadPath + fileName);

            try {
                Files.createDirectories(Path.of(uploadPath));
                tempFile.moveFileTo(destFile, true);
            } catch (Exception e) {
                return internalServerError("File upload failed: " + e.getMessage());
            }

            coffee.imageUrl = "/assets/uploads/" + fileName;
        }

        coffee.save();
        return redirect(routes.AdminController.list());
    }

    // Delete a coffee item
    public Result delete(Http.Request request, int id) {
        Coffee coffee = Coffee.find.byId(id);
        if (coffee != null) {
            coffee.delete();
        }
        return redirect(routes.AdminController.list());
    }

    // Show form to edit an existing coffee item
    public Result editForm(Http.Request request, int id) {
        Coffee coffee = Coffee.find.byId(id);
        if (coffee == null) {
            return notFound("Coffee item not found");
        }
        Form<Coffee> coffeeForm = formFactory.form(Coffee.class).fill(coffee);
        return ok(views.html.admin.edit_coffee.render(coffeeForm, coffee, request));
    }

    // Handle coffee update
    public Result update(Http.Request request, int id) {
        Form<Coffee> coffeeForm = formFactory.form(Coffee.class).bindFromRequest(request);

        if (coffeeForm.hasErrors()) {
            return badRequest(views.html.admin.edit_coffee.render(coffeeForm, Coffee.find.byId(id), request));
        }

        Coffee updatedCoffee = coffeeForm.get();
        Coffee existingCoffee = Coffee.find.byId(id);

        if (existingCoffee == null) {
            return notFound("Coffee item not found");
        }

        existingCoffee.name = updatedCoffee.name;
        existingCoffee.description = updatedCoffee.description;
        existingCoffee.price = updatedCoffee.price;
        existingCoffee.size = updatedCoffee.size;
        existingCoffee.quantity = updatedCoffee.quantity;
        existingCoffee.imageUrl = updatedCoffee.imageUrl;

        existingCoffee.update();

        return redirect(routes.AdminController.list());
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
        BigDecimal price = new BigDecimal(json.findPath("price").asText()); // ✅ Convert to BigDecimal
        String sizeString = json.findPath("size").asText();
        int quantity = json.findPath("quantity").asInt();
        String imageUrl = json.findPath("imageUrl").asText();

        Coffee coffee = Coffee.find.byId(id);
        if (coffee == null) {
            return notFound("Coffee item not found");
        }

        coffee.name = name;
        coffee.description = description;
        coffee.price = price;
        coffee.size = CoffeeSize.valueOf(sizeString.toUpperCase()); // ✅ Convert String to Enum
        coffee.quantity = quantity;
        coffee.imageUrl = imageUrl;

        coffee.update();
        return ok("Item updated successfully");
    }


}
