package controllers;

import java.util.List;

import models.Resource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDateTime;

import play.mvc.Controller;

public class Application extends Controller {

	public static final String HTTP = "http://";

	public static void index() {
		List<Resource> resources = Resource.find("order by title").fetch();
		render(resources);
	}

	public static void addResource(String url) {
		if (!StringUtils.isBlank(url) && !"null".equals(url)) {
			Resource resource = new Resource();
			if (!url.startsWith(HTTP)) {
				url = HTTP + url;
			}
			resource.url = url;
			resource.title = url;
			resource.created = new LocalDateTime();
			resource.save();
			renderJSON(resource);
		} else {
			error("Empty url");
		}
	}


}