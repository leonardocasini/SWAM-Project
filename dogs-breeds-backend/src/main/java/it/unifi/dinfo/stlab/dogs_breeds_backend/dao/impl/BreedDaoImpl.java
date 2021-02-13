package it.unifi.dinfo.stlab.dogs_breeds_backend.dao.impl;

import java.util.List;

import javax.enterprise.context.RequestScoped;

import it.unifi.dinfo.stlab.dogs_breeds_backend.dao.BreedDao;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dao.GenericDao;
import it.unifi.dinfo.stlab.dogs_breeds_backend.models.Breed;

@RequestScoped
public class BreedDaoImpl extends GenericDao<Breed> implements BreedDao{
	
	
	public BreedDaoImpl() {
		super.setClass(Breed.class);
	}
	
	@Override
	public List<Breed> getAllBreeds(){	
		return em.createQuery("SELECT b FROM Breed b", Breed.class)
				.getResultList();
	}
	
	@Override
	public List<Breed> getByCountry(String country){
		return em.createQuery("SELECT b FROM Breed b WHERE b.country = : countryB", Breed.class)
				.setParameter("countryB", country)
				.getResultList();
	}
	
	
	@Override
	public List<Breed> getBySize(String size){
		return em.createQuery("SELECT b FROM Breed b WHERE b.size =:sizeB", Breed.class)
				.setParameter("sizeB", size)
				.getResultList();
	}
	
	@Override 
	public List<Breed> getByFciGroup(String fciGroup){
		return em.createQuery("SELECT b FROM Breed b WHERE b.fciGroup =:fcigroupB", Breed.class)
				.setParameter("fciGroupB", fciGroup)
				.getResultList();		
	}
	
	@Override 
	public Breed getByName(String name){
		 return (Breed) em.createQuery("SELECT b FROM Breed b WHERE b.name =: nameB", Breed.class)
	                .setParameter("nameB",name).getSingleResult();
		
	}
			

	
	
	
}


