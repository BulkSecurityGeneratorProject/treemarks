package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Photo extends Model {

	public String url;
	
	public Photo(String url)  {
		this.url = url;
	}
			
}
