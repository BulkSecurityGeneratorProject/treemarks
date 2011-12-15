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
		List<Group> groups = Group.find("order by created desc").fetch();
		List<Resource> resources = Resource.find("order by created desc").fetch();
		render(groups, resources);
	}
	
	public static void listGroup(long groupId) {
		Group group = Group.findById(groupId);
		List<Resource> resources = Resource.find("select distinct r from Resource r join r.groups g where g.id = :groupid").bind("groupid", groupId).fetch();
		renderJSON(resources);
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
			group.created = new LocalDateTime();
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