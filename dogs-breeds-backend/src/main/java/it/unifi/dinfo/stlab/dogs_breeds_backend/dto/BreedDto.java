package it.unifi.dinfo.stlab.dogs_breeds_backend.dto;

import java.util.HashSet;
import java.util.Set;

import it.unifi.dinfo.stlab.dogs_breeds_backend.models.Breed;


public class BreedDto {
	
	private Long id;
	private String name;
	private String country;
	private String size;
	private String fciGroup;
	private String lifeExpectance;
	private String characteristics;
	private String imageURL;
	private Set<BreedingFarmDto> breedingFarms = new HashSet<>();
	
	
	public BreedDto() {}
	
	public BreedDto( Breed entity ) {
		if( entity == null) return;
		
		this.id = entity.getId();
		this.name = entity.getName();
		this.country = entity.getCountry();
		this.size = entity.getSize();
		this.fciGroup = entity.getFciGroup();
		this.lifeExpectance = entity.getLifeExpectance();
		this.characteristics = entity.getCharacteristics();
		this.imageURL = entity.getImageURL();
		
		
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

	public String getFciGroup() {
		return fciGroup;
	}

	public void setFciGroup(String fciGroup) {
		this.fciGroup = fciGroup;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public Set<BreedingFarmDto> getBreedingFarms() {
		return breedingFarms;
	}

	public void setBreedingFarms(Set<BreedingFarmDto> breedingFarms) {
		this.breedingFarms = breedingFarms;
	}
	
	public void addBreedingFarmDto(BreedingFarmDto breedingFarmDto) {
		this.breedingFarms.add(breedingFarmDto);
	}

	

}
