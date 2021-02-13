package it.unifi.dinfo.stlab.dogs_breeds_backend.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;


@Entity
public class Breed {
	
	@Id
	@GeneratedValue
	private Long id; // Id unico associato ad una razza
	
	private String name; // Nome della razza
	private String country; // Paese di provenienza della razza
	private String size; // Dimensione della razza
	private String fciGroup; // Gruppo della razza basato sulla Fédération cynologique internationale(Ente nazionale cinofilia in Italia)
	private String lifeExpectance; // Aspettativa di vita della razza
	private String characteristics; // Aspetti caratteriali di una razza
	private String imageURL; // URL immagine della razza	
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<BreedingFarm> breedingFarms = new HashSet<>();

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	
	public String getLifeExpectance() {
		return lifeExpectance;
	}
	public void setLifeExpectance(String lifeExpectance) {
		this.lifeExpectance = lifeExpectance;
	}
	
	public String getCharacteristics() {
		return characteristics;
	}
	public void setCharacteristics(String characteristics) {
		this.characteristics = characteristics;
	}
	
	public String getFciGroup() {
		return fciGroup;
	}
	public void setFciGroup(String fciGroup) {
		this.fciGroup = fciGroup;
	}
	
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	public Set<BreedingFarm> getBreedingFarms() {
		return breedingFarms;
	}
	public void setBreedingFarms(Set<BreedingFarm> breedingFarms) {
		this.breedingFarms = breedingFarms;
	}
	
	public void addBreedingFarm(BreedingFarm farm) {
		this.breedingFarms.add(farm);
	}
	
	

}
