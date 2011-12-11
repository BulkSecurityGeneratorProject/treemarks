package controllers;

import models.User;
import play.libs.OAuth;
import play.libs.OAuth.Response;
import play.libs.OAuth.ServiceInfo;
import play.mvc.Controller;

public class Login extends Controller {

	// FLICKR is a OAuth.ServiceInfo object
	private static final ServiceInfo FLICKR = new ServiceInfo(
			"http://www.flickr.com/services/oauth/request_token",
			"http://www.flickr.com/services/oauth/access_token",
			"http://www.flickr.com/services/oauth/authorize",
			"6d2c8fe7aae9133634c58c6e5063b743", "e3b9f174e6b63a85");

	public static void index() {
		render();
	}

	public static void authenticate(String username) {
		User user = getUser(username);
		if (OAuth.isVerifierResponse()) {
			// We got the verifier;
			// now get the access tokens using the request tokens
			OAuth.Response resp = OAuth.service(FLICKR).retrieveAccessToken(
					user.token, user.secret);
			// let's store them and go back to index
			user.token = resp.token;
			user.secret = resp.secret;
			user.save();
			render(user);
		}
		OAuth flickr = OAuth.service(FLICKR);
		Response resp = flickr.retrieveRequestToken();
		// We received the unauthorized tokens
		// we need to store them before continuing
		user.token = resp.token;
		user.secret = resp.secret;
		user.save();
		// Redirect the user to the authorization page
		redirect(flickr.redirectUrl(resp.token));
	}

	private static User getUser(String username) {
		User user = User.find("username = ?", username).first();
		if (user == null) {
			user = new User(username);
		}
		return user;
	}
}
