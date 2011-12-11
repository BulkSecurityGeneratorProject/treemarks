package models;

import java.util.Set;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Group extends Model {

	public String name;
	public Set<Group> parents;
}
