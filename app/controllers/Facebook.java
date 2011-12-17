package controllers;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import models.Group;
import models.Resource;
import models.User;
import play.Logger;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;
import play.mvc.Router;

public class Facebook extends Controller {

	public static final String APP_ID = "259176644136057";
	public static final String APP_SECRET = "a465e617d556c8ee0a34359129e259cf";
	public static final String redirectUri = Router
			.getFullUrl("Facebook.login");

	private static final Pattern accessTokenBodyMatcher = Pattern
			.compile("access_token=(.*)&expires=(\\d*)");

	public static void canvas() {
		redirect("https://www.facebook.com/dialog/oauth?client_id=" + APP_ID
				+ "&redirect_uri=" + redirectUri); // + "&scope=email");
	}

	public static void login(String code) {
		if (StringUtils.isNotBlank(code)) {
			Logger.info("code from facebook: %s", code);
			String accessTokenUrl = "https://graph.facebook.com/oauth/access_token?client_id="
					+ APP_ID
					+ "&redirect_uri="
					+ redirectUri
					+ "&client_secret=" + APP_SECRET + "&code=" + code;
			HttpResponse accessTokenResponse = WS.url(accessTokenUrl).get();
			String accessTokenBody = accessTokenResponse.getString();
			Logger.info("access token from facebook: %s", accessTokenBody);
			Matcher matcher = accessTokenBodyMatcher.matcher(accessTokenBody);
			if (matcher.matches()) {
				String accessToken = matcher.group(1);
				String expires = matcher.group(2);
				HttpResponse userInfoResponse = WS.url(
						"https://graph.facebook.com/me?access_token="
								+ accessToken).get();
				JsonObject facebookUserInfo = (JsonObject) userInfoResponse
						.getJson();
				String facebookUsername = facebookUserInfo.get("username")
						.toString();
				User user = User.find("where facebookUsername = ?",
						facebookUsername).first();
				if (user == null) {
					user = createFacebookUser(facebookUsername);
				}

				session.put("userId", user.id);
				redirect("Application.index");
				// render(facebookUsername, facebookUserInfo, code, accessToken,
				// expires);
			}
		} else {
			redirect("Application.index");
		}
	}

	public static void login1() {
		User user = User.all().first();
		if (user == null) {
			user = new User();
			user.save();
		}
		Group rootGroup = user.rootGroup;
		if (rootGroup == null) {
			rootGroup = new Group();
			rootGroup.save();
			user.rootGroup = rootGroup;
			user.save();
		}

		session.put("userId", user.id);
		redirect("Application.index");
	}

	private static User createFacebookUser(String facebookUserName) {
		User user = new User();
		user.facebookUsername = facebookUserName;
		Group rootGroup = new Group();
		rootGroup.save();
		user.rootGroup = rootGroup;
		user.save();
		return user;
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
