package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="users")
public class User extends Model {

	public String username;
	
	public String facebookUsername;
	
	public String secret;
	
	public String token;
	
	@OneToOne
	public Group rootGroup;

	public User() {
	}

	public User(String username) {
		this.username = username;
	}
}
