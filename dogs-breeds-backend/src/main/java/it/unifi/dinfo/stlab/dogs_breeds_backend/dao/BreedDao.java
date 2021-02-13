package it.unifi.dinfo.stlab.dogs_breeds_backend.dao;

import java.util.List;

import it.unifi.dinfo.stlab.dogs_breeds_backend.models.Breed;

public interface BreedDao {
	
	public List<Breed> getAllBreeds(); // recupera tutte le razze canine
	public List<Breed> getByCountry(String country); // recupera le razze per nazione di appartenenza
	public List<Breed> getBySize(String size); // recupera le razze per taglia
	public List<Breed> getByFciGroup(String fciGroup); // recupera le razze in base all'fciGroup
	public Breed getByName(String name); // recupera razza per nome
	
}

