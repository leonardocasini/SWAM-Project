package it.unifi.dinfo.stlab.dogs_breeds_backend.dao;

import java.util.List;

import it.unifi.dinfo.stlab.dogs_breeds_backend.models.CommKey;

public interface CommKeyDao {
	
	public List<CommKey> getAllCommKeys(); 
	public CommKey getByProvUsername(String provUsername);

}
