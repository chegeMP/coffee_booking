package controllers;

import models.Coffee;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;

import javax.inject.Inject;
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
    // Show form to create a new coffee
    // Show form to create a new coffee
    public Result createForm(Http.Request request) {  // ✅ Accept request
        Form<Coffee> coffeeForm = formFactory.form(Coffee.class);
        return ok(views.html.admin.add_coffee.render(coffeeForm, request)); // ✅ Pass request
    }




    // Handle coffee creation
    public Result create(Http.Request request) {
        Form<Coffee> coffeeForm = formFactory.form(Coffee.class).bindFromRequest(request);

        if (coffeeForm.hasErrors()) {
            return badRequest(views.html.admin.add_coffee.render(coffeeForm, request));  // ✅ Pass request
        }

        Coffee coffee = coffeeForm.get();
        coffee.save();

        return redirect(routes.AdminController.list());
    }
    // Delete a coffee item
    public Result delete(Http.Request request, int id) {  // ✅ Accept request
        Coffee coffee = Coffee.find.byId(id);
        if (coffee != null) {
            coffee.delete();
        }
        return redirect(routes.AdminController.list());
    }


}
