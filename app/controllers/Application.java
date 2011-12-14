package controllers;

import java.util.List;

import models.Group;
import models.Resource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDateTime;

import play.mvc.Controller;

public class Application extends Controller {

	public static final String HTTP = "http://";

	public static void index() {
		List<Resource> resources = Resource.find("order by created desc").fetch();
		render(resources);
	}

	public static void addResource(String url) {
		if (isText(url)) {
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

	public static void addGroup(String name) {
		if (isText(name)) {
			Group group = new Group();
			group.name = name;
			group.save();
			renderJSON(group);
		} else {
			error("Empty url");
		}
	}

	private static boolean isText(String text) {
		return !StringUtils.isBlank(text) && !"null".equals(text);
	}
}