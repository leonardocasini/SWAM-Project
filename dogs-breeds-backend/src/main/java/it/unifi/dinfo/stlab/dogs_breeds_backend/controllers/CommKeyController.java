package it.unifi.dinfo.stlab.dogs_breeds_backend.controllers;


import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dao.impl.CommKeyDaoImpl;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dto.CommKeyDto;
import it.unifi.dinfo.stlab.dogs_breeds_backend.mappers.CommKeyMapper;
import it.unifi.dinfo.stlab.dogs_breeds_backend.models.CommKey;
import it.unifi.dinfo.stlab.dogs_breeds_backend.utils.RandomStringGenerator;
import simmetric.SimmetricCryptography;

@RequestScoped
public class CommKeyController {
	
	public CommKeyController() {}
	
	@Inject private CommKeyDaoImpl commKeyDao;
	@Inject private CommKeyMapper commKeyMapper;
	
	/* Chiave interna del modulo */
	String internalPassword = "lsgCsgdld";
	SimmetricCryptography c = new SimmetricCryptography(internalPassword);
	RandomStringGenerator randomGenerator = new RandomStringGenerator();
	
	
	
	/* */
	public List<CommKeyDto> getAll(){
		
		List<CommKeyDto> commKeyDtos = new ArrayList<>();
		List<CommKey> commKeys = commKeyDao.findAll();
		
		for(int i = 0; i<commKeys.size(); i++){
			CommKeyDto dto = commKeyMapper.toDto(commKeys.get(i));
			commKeyDtos.add(dto);
		}
		return commKeyDtos;
		
		
	}
	
	/* Creation of Username chosen by the Provider, while the commKey is auto-generated*/
	public boolean saveCommKey(CommKeyDto dto){
		
		try{
						
			String provUsername = dto.getProvUsername(); // Recupero Username dal dto
			
			
			String randomValue = RandomStringGenerator.generateString();
	    				
			// Non criptiamo il provUsername perché serve per recuperare la chiave 			
			//Criptaggio della chiave auto-generata
			String encryptedValue = c.getEncriptedText(randomValue);
			
			CommKey commKey = commKeyMapper.toEntity(dto, new CommKey());
			
			commKey.setProvUsername(provUsername);
			
			commKey.setValue(encryptedValue);
			
			commKeyDao.persist(commKey);
			
			return true;
			
		} catch (Exception e) {
			
			return false;
		}
				
 	
	}
	
	public CommKeyDto getById(Long id){
		
		
		CommKey commKey = commKeyDao.findById(id);
		CommKeyDto dto = commKeyMapper.toDto(commKey);
		return dto;
		
	}
	
	public CommKeyDto getByProvUsername(String provUsername) {
		
		
		

		CommKey commKey = commKeyDao.getByProvUsername(provUsername);
		CommKeyDto dto = commKeyMapper.toDto(commKey);
		
		return dto;

	}
	

	public boolean commKeyExists(String provUsername) {
		
		CommKey commKey = commKeyDao.getByProvUsername(provUsername);
				

		if(commKeyDao.exist(commKey)) {
			
			return true;
			
		} else {
			
			return false;
		}
		
	}

}