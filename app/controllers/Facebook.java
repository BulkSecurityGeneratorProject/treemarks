package controllers;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import models.Resource;
import play.Logger;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;
import play.mvc.Router;

public class Facebook extends Controller {
	
	public static final String APP_ID = "259176644136057";
	public static final String APP_SECRET = "a465e617d556c8ee0a34359129e259cf";
	public static final String redirectUri = Router.getFullUrl("Facebook.login");
	
	public static void canvas() {
		redirect("https://www.facebook.com/dialog/oauth?client_id="+APP_ID+"&redirect_uri="+redirectUri+"&scope=email");
	}

	public static void login(String code) {
		if (StringUtils.isNotBlank(code)) {
			Logger.info("code from facebook: %s", code);
			String accessTokenUrl = "https://graph.facebook.com/oauth/access_token?client_id="+APP_ID+"&redirect_uri="+redirectUri+"&client_secret="+APP_SECRET+"&code="+code;
			HttpResponse accessTokenResponse = WS.url(accessTokenUrl).get();
			String accessToken = accessTokenResponse.getString();
			Logger.info("access token from facebook: %s", accessToken);
			render(code, accessToken);
		} else {
			redirect("Application.index");
		}
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
