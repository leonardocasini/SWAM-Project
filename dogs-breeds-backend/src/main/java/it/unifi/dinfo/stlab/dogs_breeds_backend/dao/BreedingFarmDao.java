package it.unifi.dinfo.stlab.dogs_breeds_backend.dao;

import java.util.List;

import it.unifi.dinfo.stlab.dogs_breeds_backend.models.Breed;
import it.unifi.dinfo.stlab.dogs_breeds_backend.models.BreedingFarm;

public interface BreedingFarmDao {
	
	public List<BreedingFarm> getAllBreeders();
	public List<Breed> getAllBreedOfBreedingFarm(Long breedingFarmId);
	public BreedingFarm getByName(String name); 
 	

}
