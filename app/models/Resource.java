package models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Resource extends Model {

	public String url;
	public String description;
	@ManyToMany
	public Set<Group> groups;
	@ManyToOne
	public Category category;
}
