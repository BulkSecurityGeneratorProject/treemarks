package controllers;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	private static final Pattern accessTokenBodyMatcher = Pattern.compile("access_token=(.*)&expires=(\\d*)");
	
	public static void canvas() {
		redirect("https://www.facebook.com/dialog/oauth?client_id="+APP_ID+"&redirect_uri="+redirectUri+"&scope=email");
	}

	public static void login(String code) {
		if (StringUtils.isNotBlank(code)) {
			Logger.info("code from facebook: %s", code);
			String accessTokenUrl = "https://graph.facebook.com/oauth/access_token?client_id="+APP_ID+"&redirect_uri="+redirectUri+"&client_secret="+APP_SECRET+"&code="+code;
			HttpResponse accessTokenResponse = WS.url(accessTokenUrl).get();
			String accessTokenBody = accessTokenResponse.getString();
			Logger.info("access token from facebook: %s", accessTokenBody);
			Matcher matcher = accessTokenBodyMatcher.matcher(accessTokenBody);
			if (matcher.matches()) {
				String accessToken = matcher.group(1);
				String expires = matcher.group(2);
//				render(code, accessToken, expires);
				redirect("https://graph.facebook.com/me?access_token="+accessToken);
			}
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
