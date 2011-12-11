package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="users")
public class User extends Model {

	public String username;
	public String secret;
	public String token;
	
	public User(String username) {
		this.username = username;
	}
}
