package it.unifi.dinfo.stlab.dogs_breeds_backend.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CommKey {
	
	@Id
	@GeneratedValue
	private Long id;
	private String value;
	private String provUsername;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getProvUsername() {
		return provUsername;
	}
	public void setProvUsername(String provUsername) {
		this.provUsername = provUsername;
	}
	
}
	
