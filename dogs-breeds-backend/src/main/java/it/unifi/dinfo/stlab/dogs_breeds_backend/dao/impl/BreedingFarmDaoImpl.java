package it.unifi.dinfo.stlab.dogs_breeds_backend.dao.impl;

import java.util.List;

import javax.enterprise.context.RequestScoped;

import it.unifi.dinfo.stlab.dogs_breeds_backend.dao.BreedingFarmDao;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dao.GenericDao;
import it.unifi.dinfo.stlab.dogs_breeds_backend.models.Breed;
import it.unifi.dinfo.stlab.dogs_breeds_backend.models.BreedingFarm;


@RequestScoped
public class BreedingFarmDaoImpl extends GenericDao<BreedingFarm> implements BreedingFarmDao{

	
	public BreedingFarmDaoImpl() {
		super.setClass(BreedingFarm.class);
	}
	
	@Override
	public List<BreedingFarm> getAllBreeders(){
		return em.createQuery("SELECT b FROM BreedingFarm b", BreedingFarm.class)
				.getResultList();
	}
	
	@Override
	public List<Breed> getAllBreedOfBreedingFarm(Long breedingFarmId){
		
		List<Breed> breeds = em.createQuery("SELECT b FROM Breed b WHERE b.breedingFarm.id = :bId", Breed.class)
				.setParameter("bId", breedingFarmId)
				.getResultList();
		
		return breeds;
	}
	
	@Override 
	public BreedingFarm getByName(String name){
		 return (BreedingFarm) em.createQuery("SELECT b FROM BreedingFarm b WHERE b.farmName =: nameB", BreedingFarm.class)
	                .setParameter("nameB",name).getSingleResult();
		
	}
	
	
	
}
