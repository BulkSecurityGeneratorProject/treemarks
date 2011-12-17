package controllers;

import java.util.List;

import models.Group;
import models.Resource;
import models.User;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import play.db.jpa.JPABase;
import play.mvc.Controller;

public class Application extends Controller {

	public static final String HTTP = "http://";

	public static void index() {
		Long userId = Long.valueOf(session.get("userId"));
		User user = User.findById(userId);
		Group rootGroup = user.rootGroup;
		render(rootGroup);
	}
	
	public static void listGroup(long groupId) {
		List<Group> subGroups = Resource.find("select distinct g from Group g join g.parents p where p.id = :groupid order by created desc").bind("groupid", groupId).fetch();
		List<Resource> resources = Resource.find("select distinct r from Resource r join r.groups g where g.id = :groupid").bind("groupid", groupId).fetch();
		JsonObject aggregate = new JsonObject();
		aggregate.add("subgroups", new Gson().toJsonTree(subGroups));
		aggregate.add("resources", new Gson().toJsonTree(resources));
		renderJSON(aggregate);
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