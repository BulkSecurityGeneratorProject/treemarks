package models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import play.db.jpa.Model;

@Entity
@Table(name="groups")
public class Group extends Model {

	public String name;

	@ManyToMany
	public Set<Group> parents;

	@ManyToOne
	public User user;
	
	@Type(type="org.joda.time.contrib.hibernate.PersistentLocalDateTime")
	public LocalDateTime created;
}
