package controllers;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.*;

public class HomeController extends Controller {
        public Result index(Http.Request request) {
            return ok(index.render(request));
        }

    public Result menu(Http.Request request) {
        return ok(menu.render(request));
    }

    public Result cart(Http.Request request) {
        return ok(cart.render(request));
    }
    public Result checkout(Http.Request request) {
        return ok(checkout.render(request));
    }

}
