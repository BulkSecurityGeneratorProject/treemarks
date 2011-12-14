package controllers;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import models.Resource;
import play.mvc.Controller;
import play.mvc.Router;

public class Facebook extends Controller {
	
	public static final String APP_ID = "259176644136057";
	public static final String APP_SECRET = "a465e617d556c8ee0a34359129e259cf";
	
	public static void canvas() {
		redirect("https://www.facebook.com/dialog/oauth?client_id="+APP_ID+"&redirect_uri=https://treemarks.com/");
	}

	
	public static void tabread() {
		render();
	}

	public static void tabedit() {
		render();
	}

	public static void channel() {
		render();
	}

}
