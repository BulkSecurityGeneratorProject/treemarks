package models;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import play.db.jpa.Model;

@Entity
public class Resource extends Model {

	public String url;
	
	public String title;
	
	public String description;
	
	 @Type(type="org.joda.time.contrib.hibernate.PersistentLocalDateTime")
//	@Temporal(TemporalType.TIMESTAMP)
	public LocalDateTime created;
	
	@ManyToMany
	public Set<Group> groups;

	@ManyToOne
	public Category category;

	@ManyToOne
	public User user;
}
