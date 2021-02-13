package it.unifi.dinfo.stlab.dogs_breeds_backend.dto;

import java.util.HashSet;
import java.util.Set;

import it.unifi.dinfo.stlab.dogs_breeds_backend.models.Breed;

public class BreedingFarmDto {
	
	private Long id; 
	private String farmName;
	private String address;
	private String telephone;

	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFarmName() {
		return farmName;
	}
	public void setFarmName(String farmName) {
		this.farmName = farmName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	
}
