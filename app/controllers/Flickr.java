package controllers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Pattern;

import models.Photo;
import play.libs.OAuth;
import play.libs.OAuth.Response;
import play.libs.OAuth.ServiceInfo;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;

public class Flickr extends Controller {

	private static final String API_KEY = "83bca224f57d1e6fe58e1124867d82fb";
	private static final String API_SIGNATURE = "2a565bb73e0bf9b6";
	private static final String PERMS = "read";
	
	private static String signature = null;

	private static Pattern flickrJson = Pattern
			.compile("jsonFlickrApi\\((.*)\\)");

	public static void index() {
		List<Photo> photos = Photo.findAll();
		render(photos, API_KEY);
	}

	public static void setUser(String flickrUser) {
		System.out.println("flickrUser" + flickrUser);
		HttpResponse response = WS
				.url("http://api.flickr.com/services/rest/?method=flickr.test.echo&api_key=%s&format=json&name=%s",
						API_KEY, flickrUser).get();

		String almostJson = response.getString();
		renderJSON(completeJson(almostJson));
	}
	
	public static void getPublicPhotosCurrentUser() {
		String method1 = "flickr.tags.getListUser";
		HttpResponse response = WS.url("http://api.flickr.com/services/rest/?api_key=%s&method=%s&format=json", API_KEY, method1).get();
		String almostJson = response.getString();
		String json = completeJson(almostJson);
		renderJSON(json);

//		JsonElement jsonObject = new JsonParser().parse(json);
//		System.out.println(jsonObject.getAsJsonObject().get(""));
//		String method2 = "flickr.people.getPublicPhotos";
	}
	
	public static void getPhotosForLocation(double latitude, double longitude) {
		HttpResponse response = WS
				.url("http://api.flickr.com/services/rest/?method=flickr.photos.geo.photosForLocation&api_key=%s&format=json&lat=%s&lon=%s",
						API_KEY, String.valueOf(latitude), String.valueOf(longitude)).get();

		String almostJson = response.getString();
		String json = almostJson.substring(14, almostJson.length() - 1);
//		String json = flickrJson.matcher(almostJson).group(1);
		renderJSON(json);
	}
	
	private static void createSignature() {
		String input = API_SIGNATURE+"api_key"+API_KEY+"perms"+PERMS;
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte [] sum = digest.digest(input.getBytes());
//			StringBuilder sb = new StringBuilder();
//			for ( byte b : sum) {
//				sb.append(Integer.toHexString(b));
//			}
			BigInteger bigInt = new BigInteger(1, sum);
			String output = bigInt.toString(16);
			System.out.println("MD5: " + output);
			signature = output;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	private static String completeJson(String almostJson) {
//		String json = flickrJson.matcher(almostJson).group(1);
		return almostJson.substring(14, almostJson.length() - 1);
	}

	/*
	 * $('#fetch').click(function() { //jQuery('#a-link').remove();
	 * 
	 * //jQuery('<img alt="">').attr('id', 'loader').attr('src',
	 * 'ajax-loader.gif').appendTo('#image-container');
	 * 
	 * //assign your api key equal to a variable var apiKey =
	 * '83bca224f57d1e6fe58e1124867d82fb';
	 * 
	 * var photosetId = '72157605321269707';
	 * 
	 * //the initial json request to flickr //to get your latest public photos,
	 * use this request:
	 * http://api.flickr.com/services/rest/?&method=flickr.people
	 * .getPublicPhotos&api_key=' + apiKey +
	 * '&user_id=29096781@N02&per_page=15&page=2&format=json&jsoncallback=?
	 * $.getJSON(
	 * 'http://api.flickr.com/services/rest/?&method=flickr.photosets.getPhotos&api_key='
	 * + apiKey + '&photoset_id=' + photosetId + '&format=json&jsoncallback=?',
	 * function(data) { $.each(data.items, function(i, item) {
	 * $("<img/>").attr("src", item.media.m).appendTo("#images"); if (i == 3 )
	 * return false; } } })
	 * 
	 * })
	 */
}
