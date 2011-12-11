package models;

import java.util.Set;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Resource extends Model {

	public String url;
	public String description;
	public Set<Group> groups;
	public Category category;
}
