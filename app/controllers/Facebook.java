package controllers;

import play.mvc.Controller;
import play.mvc.Router;

public class Facebook extends Controller {
	
	public static final String APP_ID = "305420729478595";

	public static void index() {
		render();
	}
	
	public static void login() {
		redirect("https://www.facebook.com/dialog/oauth?client_id=305420729478595&redirect_uri=https://warm-autumn-6309.herokuapp.com/Facebook/index");
	}

	public static void channel() {
		render();
	}

}
