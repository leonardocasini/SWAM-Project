package it.unifi.dinfo.stlab.dogs_breeds_backend.dao.impl;

import java.util.List;

import javax.enterprise.context.RequestScoped;

import it.unifi.dinfo.stlab.dogs_breeds_backend.dao.GenericDao;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dao.CommKeyDao;
import it.unifi.dinfo.stlab.dogs_breeds_backend.models.CommKey;

@RequestScoped
public class CommKeyDaoImpl extends GenericDao<CommKey> implements CommKeyDao{
	
	public CommKeyDaoImpl() {
		super.setClass(CommKey.class);
	}
	
	@Override
	public List<CommKey> getAllCommKeys(){	
		return em.createQuery("SELECT k FROM Key k", CommKey.class)
				.getResultList();
	}
	
	@Override 
	public CommKey getByProvUsername(String provUsername) {
		return (CommKey) em.createQuery("SELECT c FROM CommKey c WHERE c.provUsername =:provIdC")
                .setParameter("provIdC",provUsername).getSingleResult();
	}
		
	
	

}
