package it.unifi.dinfo.stlab.dogs_breeds_backend.controllers;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dao.impl.BreedingFarmDaoImpl;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dto.BreedingFarmDto;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dto.CommKeyDto;
import it.unifi.dinfo.stlab.dogs_breeds_backend.mappers.BreedingFarmMapper;
import it.unifi.dinfo.stlab.dogs_breeds_backend.models.BreedingFarm;
import simmetric.SimmetricCryptography;

@RequestScoped
public class BreedingFarmController {
	
	public BreedingFarmController() {}
	
	@Inject private BreedingFarmDaoImpl breedingFarmDao;
	@Inject private BreedingFarmMapper breedingFarmMapper;
	@Inject CommKeyController commKeyController;
	
	/* Chiave interna del modulo */
	
	String internalPassword = "lsgCsgdld";
	SimmetricCryptography c = new SimmetricCryptography(internalPassword);

	
	
	public List<BreedingFarmDto> getAll(){
		
		List<BreedingFarmDto> breedingFarmDtos = new ArrayList<>();
		List<BreedingFarm> breedingFarms = breedingFarmDao.findAll();
		
		for (int i = 0; i< breedingFarms.size(); i++) {
			BreedingFarmDto dto = breedingFarmMapper.toDto(breedingFarms.get(i));
			breedingFarmDtos.add(dto);
		}
		
		for(int n = 0 ; n< breedingFarmDtos.size(); n++) {
			
			Long id = breedingFarmDtos.get(n).getId();
			String encryptedFarmName = breedingFarmDtos.get(n).getFarmName();
			String encryptedAddress = breedingFarmDtos.get(n).getAddress();
			String encryptedTelephone = breedingFarmDtos.get(n).getTelephone();
									
	
			
			breedingFarmDtos.get(n).setId(id);
			breedingFarmDtos.get(n).setFarmName(c.getDecryptedText(encryptedFarmName));	
			breedingFarmDtos.get(n).setAddress(c.getDecryptedText(encryptedAddress));
			breedingFarmDtos.get(n).setTelephone(c.getDecryptedText(encryptedTelephone));
			
			
		}
		
		return breedingFarmDtos;
		
		
		
	}
	

	public Long createBreedingFarm(BreedingFarmDto dto, String provUsername) {
		
		/*Vado a recuperare la chiave salvata relativa al provider */		 
		CommKeyDto commKeyDto = commKeyController.getByProvUsername(provUsername);
		String commKeyEncryptedValue = commKeyDto.getValue();	
		String commKeyProvider = c.getDecryptedText(commKeyEncryptedValue);
		
		SimmetricCryptography scProvider = new SimmetricCryptography(commKeyProvider);
		
		BreedingFarmDto decryptedDto = new BreedingFarmDto();
		
		decryptedDto.setFarmName(scProvider.getDecryptedText(dto.getFarmName()));
		decryptedDto.setAddress(scProvider.getDecryptedText(dto.getAddress()));
		decryptedDto.setTelephone(scProvider.getDecryptedText(dto.getTelephone()));
		
		BreedingFarmDto encryptedDto = encryptBreedingFarmWithInternalKey(decryptedDto);
		
		BreedingFarm breedingFarm = breedingFarmMapper.toEntity(encryptedDto);
		
		breedingFarmDao.persist(breedingFarm);
		
		return breedingFarm.getId();		
		
	}
	
	
	public BreedingFarmDto getById(Long id) {
		
		BreedingFarm breedingFarm = breedingFarmDao.findById(id);
		BreedingFarmDto breedingFarmDto = breedingFarmMapper.toDto(breedingFarm);
		
		return breedingFarmDto;
		
	}
	
	public BreedingFarmDto encryptBreedingFarmWithInternalKey(BreedingFarmDto b) {
		
		String farmName = b.getFarmName();
		String telephone = b.getTelephone();
		String address = b.getAddress();
		/* Effettuo la crittografia con la nostra chiave interna */
		String encryptedFarmName = c.getEncriptedText(farmName);
		String encryptedTelephone = c.getEncriptedText(telephone);
		String encryptedAddress = c.getEncriptedText(address);
		
		BreedingFarmDto encryptedDto = new BreedingFarmDto();
		encryptedDto.setFarmName(encryptedFarmName);
		encryptedDto.setAddress(encryptedAddress);
		encryptedDto.setTelephone(encryptedTelephone);
		
		
		return encryptedDto;
		
		
	}
	
	
	
	public BreedingFarmDto getByName(String name, String provUsername) {
		
		String cryptedName = c.getEncriptedText(name);
		
		BreedingFarm breedingFarm = breedingFarmDao.getByName(cryptedName);
		BreedingFarmDto dto = breedingFarmMapper.toDto(breedingFarm);
		
		BreedingFarmDto decrypted = decryptWithInternalKey(dto);
		
		BreedingFarmDto encrypted = encryptWithProvUsername(decrypted, provUsername);
		
		return encrypted;
	}
	
	public BreedingFarmDto encryptWithProvUsername(BreedingFarmDto dto, String provUsername) {
		
		/*Vado a recuperare la chiave salvata relativa al provider */		 
		CommKeyDto commKeyDto = commKeyController.getByProvUsername(provUsername);
		String commKeyEncryptedValue = commKeyDto.getValue();	
		String commKeyProvider = c.getDecryptedText(commKeyEncryptedValue);
		
		SimmetricCryptography scProvider = new SimmetricCryptography(commKeyProvider);
		
		BreedingFarmDto encryptedDto = new BreedingFarmDto();
		encryptedDto.setId(dto.getId());
		encryptedDto.setFarmName(scProvider.getEncriptedText(dto.getFarmName()));
		encryptedDto.setAddress(scProvider.getEncriptedText(dto.getAddress()));
		encryptedDto.setTelephone(scProvider.getEncriptedText(dto.getTelephone()));
		
		return encryptedDto;
		
	}
	
	
	public BreedingFarmDto decryptWithInternalKey(BreedingFarmDto dto){
		
		BreedingFarmDto decryptedDto = new BreedingFarmDto();
		
		decryptedDto.setFarmName(c.getDecryptedText(dto.getFarmName()));
		decryptedDto.setId(dto.getId());
		decryptedDto.setAddress(c.getDecryptedText(dto.getAddress()));
		decryptedDto.setTelephone(c.getDecryptedText(dto.getTelephone()));
		
		return decryptedDto;
		
		
	}
}



