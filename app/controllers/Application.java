package controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import models.Group;
import models.Resource;
import models.User;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDateTime;

import play.mvc.Controller;

public class Application extends Controller {

	public static final String HTTP = "http://";

	public static void index() {
		String userIdString = session.get("userId");
		if (!StringUtils.isEmpty(userIdString)) {
			Long userId = Long.valueOf(userIdString);
			User user = User.findById(userId);
			Group rootGroup = user.rootGroup;
			render(rootGroup);
		} else {
			render("@Application.login");
		}
	}

	public static void logout() {
		session.remove("userId");
		render("@Application.login");
	}
	
	public static void listSubGroups(long groupId) {
		List<Group> subgroups = Resource.find("select distinct g from Group g join g.parents p where p.id = :groupid order by g.created desc").bind("groupid", groupId).fetch();
		renderJSON(subgroups);
	}

	public static void listResources(long groupId) {
		List<Resource> resources = Resource.find("select distinct r from Resource r join r.groups g where g.id = :groupid").bind("groupid", groupId).fetch();
		renderJSON(resources);
	}
	
	public static void getAsFirefoxBackup() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream resource = Application.class.getResourceAsStream("bookmarks-2011-12-18.json");
		try {
			IOUtils.copy(resource, baos);
			renderJSON(baos.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void addResource(Long groupId, String url) {
		if (isText(url)) {
			Group group = Group.findById(groupId);
			Resource resource = new Resource();
			if (!url.startsWith(HTTP)) {
				url = HTTP + url;
			}
			resource.url = url;
			resource.title = url;
			resource.created = new LocalDateTime();
			resource.groups = new HashSet<Group>();
			resource.groups.add(group);
			resource.save();
			renderJSON(resource);
		} else {
			error("Empty url");
		}
	}

	public static void addGroup(Long parentGroupId, String name) {
		if (isText(name)) {
			Group parent = Group.findById(parentGroupId);
			Group group = new Group();
			group.name = name;
			group.created = new LocalDateTime();
			group.parents = new HashSet<Group>();
			group.parents.add(parent);
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