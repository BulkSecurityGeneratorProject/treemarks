package models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import play.db.jpa.Model;

@Entity
public class Group extends Model {

	public String name;
	@ManyToMany
	public Set<Group> parents;
}
