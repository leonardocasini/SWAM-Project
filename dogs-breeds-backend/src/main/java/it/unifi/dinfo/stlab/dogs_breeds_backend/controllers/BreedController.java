package it.unifi.dinfo.stlab.dogs_breeds_backend.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dao.impl.BreedDaoImpl;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dao.impl.BreedingFarmDaoImpl;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dto.BreedDto;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dto.BreedingFarmDto;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dto.CommKeyDto;
import it.unifi.dinfo.stlab.dogs_breeds_backend.mappers.BreedMapper;
import it.unifi.dinfo.stlab.dogs_breeds_backend.mappers.BreedingFarmMapper;
import it.unifi.dinfo.stlab.dogs_breeds_backend.models.Breed;
import it.unifi.dinfo.stlab.dogs_breeds_backend.models.BreedingFarm;
import simmetric.SimmetricCryptography;


@RequestScoped
public class BreedController {
	
	
	public BreedController() {}
	
	@Inject private BreedDaoImpl breedDao;
	@Inject private BreedMapper breedMapper;
	@Inject CommKeyController commKeyController;
	@Inject private BreedingFarmDaoImpl breedingFarmDao;
	@Inject private BreedingFarmMapper breedingFarmMapper;
	
	
	/* Chiave interna del modulo */
	
	String internalPassword = "lsgCsgdld";
	SimmetricCryptography c = new SimmetricCryptography(internalPassword);

	
	/*Returns Dtos decrypted with our internalKey*/
	public List<BreedDto> getAll() {
			
		List<BreedDto> breedDtos = new ArrayList<>();
		List<Breed> breeds = breedDao.findAll();
		
		for(int i = 0; i<breeds.size(); i++){
			BreedDto dto = breedMapper.toDto(breeds.get(i));
			breedDtos.add(dto);
		}
		//Decrpyt dtos with internalKey
		List<BreedDto> decryptedDtos = decryptBreedsWithOurInternalKey(breedDtos);
		
		return decryptedDtos;
	}
	
	/*Returns Dtos encrypted with commKey*/
	public List<BreedDto> getAllByProvUsername(String provUsername) {
		
		List<BreedDto> breedDtos = new ArrayList<>();
		List<Breed> breeds = breedDao.findAll();
		
		for(int i = 0; i<breeds.size(); i++){
			BreedDto dto = breedMapper.toDto(breeds.get(i));
			breedDtos.add(dto);
		}		
		//Decrypt of dto with my InternalKey
		List<BreedDto> decryptedDtos = decryptBreedsWithOurInternalKey(breedDtos);
		//Crypting dtos with commKey
		List<BreedDto> encryptedDtos = encryptListofBreedsByProvUsername(decryptedDtos, provUsername);
			
		return encryptedDtos;						
	}
	
	
	
	public BreedDto getById(Long id, String provUsername) {
	
		/* Get breed encrypted */
		Breed breed = breedDao.findById(id);
		// Dto criptato
		BreedDto dto = breedMapper.toDto(breed);
		// Decrypt dto with internalKey
		BreedDto decryptedDto = decryptWithInternalKey(dto);
		//Crypting dto with commKey
		BreedDto encryptedDto = encryptBreedByProvUsername(decryptedDto, provUsername);

		return encryptedDto;
					
	}
		
	public Long createBreed(BreedDto dto, String provUsername){	
		/* If breed with this name already exists, breedFound = true, return noContent */	
		boolean breedFound = false;	
//		List<BreedDto> encryptedbreedDtos = getAll();				
//		for(int i = 0; i< encryptedbreedDtos.size(); i++) {
//								
//			if(encryptedbreedDtos.get(i).getName().equals(dto.getName())){
//				breedFound = true;			
//			}		
//		}
		
		/*If breedFound = false, create this new Breed */
//		if(breedFound == false) {
			// Decrypt dto received with its commKey		
			BreedDto decryptedDto = decryptBreedByProvUsername(dto, provUsername);	
			// Encrypt dto with our internalKey
			BreedDto cryptedBreedDto = encryptBreedWithInternalKey(decryptedDto);	
			/* Breed encrypted will be persisted in DBMS */
			Breed breed = breedMapper.toEntity(cryptedBreedDto, new Breed());
			
			
			//Breed breed = breedMapper.toEntity(dto, new Breed());
			
		
			
	
			/*Persist Breed crypted*/
			breedDao.persist(breed);
	 
			return breed.getId();		
//		} else {			
//			return null;
//		}				
	}
	
	public boolean deleteBreed(Long breedId) {	
		// If no breed exists with this Id return false
		if( !breedDao.existsId( breedId ) ) {	
			return false;		
		} else {		
			breedDao.delete(breedId);		
			return true;			
		}	
	}
	
	
	
	public List<BreedDto> getByCountry(String country, String provUsername){
			
		/*Restituisce le razze che hanno come attributo country criptato*/
		List<BreedDto> breedDtos = new ArrayList<>();
		String cryptedCountry = c.getEncriptedText(country);
		List<Breed> breeds = breedDao.getByCountry(cryptedCountry);
		
		for(int i = 0; i<breeds.size(); i++){
			BreedDto dto = breedMapper.toDto(breeds.get(i));
			breedDtos.add(dto);
		}
		//Decrypting with our InternalKey
		List<BreedDto> decryptedBreedDtos = decryptBreedsWithOurInternalKey(breedDtos);
		//Crypting with commKey
		List<BreedDto> breedDtosEncrypted= encryptListofBreedsByProvUsername(decryptedBreedDtos, provUsername);
		
		return breedDtosEncrypted;
		
	}
	
	public List<BreedDto> getBySize(String size, String provUsername){
		
		/*Restituisce le razze che hanno come attributo country criptato*/
		List<BreedDto> breedDtos = new ArrayList<>();
		String cryptedSize = c.getEncriptedText(size);
		List<Breed> breeds = breedDao.getByCountry(cryptedSize);
		
		for(int i = 0; i<breeds.size(); i++){
			BreedDto dto = breedMapper.toDto(breeds.get(i));
			breedDtos.add(dto);
		}
		
		//Decrypting with our InternalKey
		List<BreedDto> decryptedBreedDtos = decryptBreedsWithOurInternalKey(breedDtos);
		//Crypting with commKey
		List<BreedDto> breedDtosEncrypted= encryptListofBreedsByProvUsername(decryptedBreedDtos, provUsername);
		
		return breedDtosEncrypted;
		
	}
	
	public List<BreedDto> getByFciGroup(String fciGroup, String provUsername){
		
		/*Restituisce le razze che hanno come attributo country criptato*/
		List<BreedDto> breedDtos = new ArrayList<>();
		String cryptedFciGroup = c.getEncriptedText(fciGroup);
		List<Breed> breeds = breedDao.getByCountry(cryptedFciGroup);
		
		for(int i = 0; i<breeds.size(); i++){
			BreedDto dto = breedMapper.toDto(breeds.get(i));
			breedDtos.add(dto);
		}
		
		//Decrypting with our InternalKey
		List<BreedDto> decryptedBreedDtos = decryptBreedsWithOurInternalKey(breedDtos);
		//Crypting with commKey
		List<BreedDto> breedDtosEncrypted= encryptListofBreedsByProvUsername(decryptedBreedDtos, provUsername);
		
		return breedDtosEncrypted;
	}
	
	public BreedDto getByName(String name, String provUsername) {
		
		
		String cryptedName = c.getEncriptedText(name);
		
		Breed breed = breedDao.getByName(cryptedName);
		BreedDto dto = breedMapper.toDto(breed);
			
		BreedDto dtoDecrypted = decryptWithInternalKey(dto);
	
		// Encrypting Breed found with provider's commKey 
		BreedDto encryptedDtoByCommKeyProvider = encryptBreedByProvUsername(dtoDecrypted, provUsername);
				
		return encryptedDtoByCommKeyProvider;

	}
		
	public BreedDto updateBreed(BreedDto dto, String provUsername) {
				
		BreedDto decryptedDto = decryptBreedByProvUsername(dto, provUsername);
		
		
		
		Long id = decryptedDto.getId();
		String name = decryptedDto.getName();
		String country = decryptedDto.getCountry();
		String size = decryptedDto.getSize();
		String lifeExpectance = decryptedDto.getLifeExpectance();
		String imageURL = decryptedDto.getImageURL();
		String fciGroup = decryptedDto.getFciGroup();
		String characteristics = decryptedDto.getCharacteristics();
		Set<BreedingFarmDto> farms = decryptedDto.getBreedingFarms();
		//Set<BreedingFarmDto> farms = dto.getBreedingFarms();

	
		/* Effettuo la crittografia con la nostra chiave interna */
		String cryptedName = c.getEncriptedText(name);
		String cryptedCountry = c.getEncriptedText(country);
		String cryptedSize = c.getEncriptedText(size);
		String cryptedLifeExp = c.getEncriptedText(lifeExpectance);
		String cryptedImageURL = c.getEncriptedText(imageURL);
		String cryptedFciGroup = c.getEncriptedText(fciGroup);
		String cryptedCharacteristics = c.getEncriptedText(characteristics);
		
		/*Encrypting farms*/
		Set<BreedingFarmDto> encryptedFarms = farms.stream().map(farm -> {
			BreedingFarmDto breedingFarmDto = new BreedingFarmDto();
			
			breedingFarmDto.setId(farm.getId());
			breedingFarmDto.setFarmName(c.getEncriptedText(farm.getFarmName()));
			breedingFarmDto.setAddress(c.getEncriptedText(farm.getAddress()));
			breedingFarmDto.setTelephone(c.getEncriptedText(farm.getTelephone()));
			
			return breedingFarmDto;
				
		}).collect(Collectors.toSet());
		

		
		Breed breed = breedDao.findById(dto.getId());
		
		breed.setName(cryptedName);
		breed.setSize(cryptedSize);
		breed.setCharacteristics(cryptedCharacteristics);
		breed.setFciGroup(cryptedFciGroup);
		breed.setImageURL(cryptedImageURL);
		breed.setLifeExpectance(cryptedLifeExp);
		breed.setCountry(cryptedCountry);
		breed.setBreedingFarms(encryptedFarms.stream().map(breedingFarmMapper::toEntity).collect(Collectors.toSet())
			);
	

		/*Modifico il Breed criptato*/
		breedDao.update(breed);
		
		return breedMapper.toDto(breed);
 
			
		
	}
	
	public Set<BreedingFarmDto> getBreedingFarmOfBreed(Long id, String provUsername){
		
		/* Get breed encrypted */
		Breed breed = breedDao.findById(id);
		// Dto criptato
		BreedDto dto = breedMapper.toDto(breed);
		// Decrypt dto with internalKey
		BreedDto decryptedDto = decryptWithInternalKey(dto);
		
		Set<BreedingFarmDto> breedingfarms = decryptedDto.getBreedingFarms();
		
		Set<BreedingFarmDto> encryptedFarms = new HashSet<>();
		//Crypting dto with commKey
		
		/*Vado a recuperare la chiave salvata relativa al provider */		 
		CommKeyDto commKeyDto = commKeyController.getByProvUsername(provUsername);
		String commKeyEncryptedValue = commKeyDto.getValue();	
		String commKeyProvider = c.getDecryptedText(commKeyEncryptedValue);

		SimmetricCryptography scProvider = new SimmetricCryptography(commKeyProvider);
		
		for(int i = 0; i < breedingfarms.size(); i++) {
			
			/*Encrypting farms*/
			encryptedFarms = breedingfarms.stream().map(farm -> {
				BreedingFarmDto breedingFarmDto = new BreedingFarmDto();
				
				breedingFarmDto.setId(farm.getId());
				breedingFarmDto.setFarmName(scProvider.getEncriptedText(farm.getFarmName()));
				breedingFarmDto.setAddress(scProvider.getEncriptedText(farm.getAddress()));
				breedingFarmDto.setTelephone(scProvider.getEncriptedText(farm.getTelephone()));
				
				return breedingFarmDto;
					
			}).collect(Collectors.toSet());
			
		}
		
		return encryptedFarms;
				
	}
	
	
	public BreedDto decryptBreedByProvUsername(BreedDto dto, String provUsername) {
		
		/* Vado a recuperare la chiave salvata relativa al provider 
		 * perché devo andare a decifrare la body con la sua chiave specifica 
		 */		 
		CommKeyDto commKeyDto = commKeyController.getByProvUsername(provUsername);
		String commKeyEncryptedValue = commKeyDto.getValue();
		String commKeyDecryptedvalue = c.getDecryptedText(commKeyEncryptedValue); // Chiave di comunicazione
		
		/* Effettuo la decifratura con la sua chiave di comunicazione */		
		SimmetricCryptography scExternal = new SimmetricCryptography(commKeyDecryptedvalue);
		
		
		String decryptedName = scExternal.getDecryptedText(dto.getName());
		String decryptedCountry = scExternal.getDecryptedText(dto.getCountry());
		String decryptedlifeExpectance = scExternal.getDecryptedText(dto.getLifeExpectance());
		String decryptedSize = scExternal.getDecryptedText(dto.getSize());
		String decryptedCharacteristics = scExternal.getDecryptedText(dto.getCharacteristics());
		String decryptedImageUrl = scExternal.getDecryptedText(dto.getImageURL());
		String decryptedFciGroup = scExternal.getDecryptedText(dto.getFciGroup());
		/*Decrypting farms*/
		Set<BreedingFarmDto> decryptedFarms = dto.getBreedingFarms().stream().map(farm -> {
			BreedingFarmDto breedingFarmDto = new BreedingFarmDto();
			
			breedingFarmDto.setId(farm.getId());
			breedingFarmDto.setFarmName(scExternal.getDecryptedText(farm.getFarmName()));
			breedingFarmDto.setAddress(scExternal.getDecryptedText(farm.getAddress()));
			breedingFarmDto.setTelephone(scExternal.getDecryptedText(farm.getTelephone()));
			
			return breedingFarmDto;
				
		}).collect(Collectors.toSet());
		
		
		BreedDto decryptedDto = new BreedDto();
		decryptedDto.setName(decryptedName);
		decryptedDto.setCountry(decryptedCountry);
		decryptedDto.setSize(decryptedSize);
		decryptedDto.setCharacteristics(decryptedCharacteristics);
		decryptedDto.setLifeExpectance(decryptedlifeExpectance);
		decryptedDto.setFciGroup(decryptedFciGroup);
		decryptedDto.setImageURL(decryptedImageUrl);
		decryptedDto.setBreedingFarms(decryptedFarms);
		
		return decryptedDto;
		
		
	}
	
	public BreedDto encryptBreedByProvUsername(BreedDto dto, String provUsername) {
		
		/*Vado a recuperare la chiave salvata relativa al provider */		 
		CommKeyDto commKeyDto = commKeyController.getByProvUsername(provUsername);
		String commKeyEncryptedValue = commKeyDto.getValue();	
		String commKeyProvider = c.getDecryptedText(commKeyEncryptedValue);

		SimmetricCryptography scProvider = new SimmetricCryptography(commKeyProvider);
		
		/* Crypting Breed found with provider's commKey */
		Long id = dto.getId();
		String encryptedNameByCommKeyProvider = scProvider.getEncriptedText(dto.getName());
		String encryptedCountryByCommKeyProvider = scProvider.getEncriptedText(dto.getCountry());
		String encryptedSizeByCommKeyProvider = scProvider.getEncriptedText(dto.getSize());
		String encryptedLifeExpByCommKeyProvider = scProvider.getEncriptedText(dto.getLifeExpectance());
		String encryptedImageURLByCommKeyProvider = scProvider.getEncriptedText(dto.getImageURL());
		String encryptedFciGroupByCommKeyProvider = scProvider.getEncriptedText(dto.getFciGroup());
		String encryptedCharacteristicsByCommKeyProvider = scProvider.getEncriptedText(dto.getCharacteristics());
		/*Encrypting farms*/
		Set<BreedingFarmDto> encryptedFarms = dto.getBreedingFarms().stream().map(farm -> {
			BreedingFarmDto breedingFarmDto = new BreedingFarmDto();
			
			breedingFarmDto.setId(farm.getId());
			breedingFarmDto.setFarmName(scProvider.getEncriptedText(farm.getFarmName()));
			breedingFarmDto.setAddress(scProvider.getEncriptedText(farm.getAddress()));
			breedingFarmDto.setTelephone(scProvider.getEncriptedText(farm.getTelephone()));
			
			return breedingFarmDto;
				
		}).collect(Collectors.toSet());
		
		
		BreedDto encryptedDtoByCommKeyProvider = new BreedDto();
		encryptedDtoByCommKeyProvider.setId(id);
		encryptedDtoByCommKeyProvider.setName(encryptedNameByCommKeyProvider);
		encryptedDtoByCommKeyProvider.setCountry(encryptedCountryByCommKeyProvider);
		encryptedDtoByCommKeyProvider.setSize(encryptedSizeByCommKeyProvider);
		encryptedDtoByCommKeyProvider.setFciGroup(encryptedFciGroupByCommKeyProvider);
		encryptedDtoByCommKeyProvider.setCharacteristics(encryptedCharacteristicsByCommKeyProvider);
		encryptedDtoByCommKeyProvider.setLifeExpectance(encryptedLifeExpByCommKeyProvider);
		encryptedDtoByCommKeyProvider.setImageURL(encryptedImageURLByCommKeyProvider);	
		encryptedDtoByCommKeyProvider.setBreedingFarms(encryptedFarms);
		
		return encryptedDtoByCommKeyProvider;
		
	}
	
	public List<BreedDto> encryptListofBreedsByProvUsername(List<BreedDto> dtos, String provUsername){
		
		/*Vado a recuperare la chiave salvata relativa al provider */		 
		CommKeyDto commKeyDto = commKeyController.getByProvUsername(provUsername);
		String commKeyEncryptedValue = commKeyDto.getValue();	
		String commKeyProvider = c.getDecryptedText(commKeyEncryptedValue);
		
		SimmetricCryptography scProvider = new SimmetricCryptography(commKeyProvider);
		
		List<BreedDto> encryptedDtos = new ArrayList<BreedDto>();
		
		for( int i = 0; i < dtos.size() ; i++) {
			
			Long id = dtos.get(i).getId();
			String encryptedNameByCommKeyProvider = scProvider.getEncriptedText(dtos.get(i).getName());
			String encryptedCountryByCommKeyProvider = scProvider.getEncriptedText(dtos.get(i).getCountry());
			String encryptedSizeByCommKeyProvider = scProvider.getEncriptedText(dtos.get(i).getSize());
			String encryptedLifeExpByCommKeyProvider = scProvider.getEncriptedText(dtos.get(i).getLifeExpectance());
			String encryptedImageURLByCommKeyProvider = scProvider.getEncriptedText(dtos.get(i).getImageURL());
			String encryptedFciGroupByCommKeyProvider = scProvider.getEncriptedText(dtos.get(i).getFciGroup());
			String encryptedCharacteristicsByCommKeyProvider = scProvider.getEncriptedText(dtos.get(i).getCharacteristics());
			/*Encrypting farms*/
			Set<BreedingFarmDto> encryptedFarms = dtos.get(i).getBreedingFarms().stream().map(farm -> {
				BreedingFarmDto breedingFarmDto = new BreedingFarmDto();
				
				breedingFarmDto.setId(farm.getId());
				breedingFarmDto.setFarmName(scProvider.getEncriptedText(farm.getFarmName()));
				breedingFarmDto.setAddress(scProvider.getEncriptedText(farm.getAddress()));
				breedingFarmDto.setTelephone(scProvider.getEncriptedText(farm.getTelephone()));
				
				return breedingFarmDto;
					
			}).collect(Collectors.toSet());
			
			
			
			BreedDto encryptedDtoByCommKeyProvider = new BreedDto();
			encryptedDtoByCommKeyProvider.setId(id);
			encryptedDtoByCommKeyProvider.setName(encryptedNameByCommKeyProvider);
			encryptedDtoByCommKeyProvider.setCountry(encryptedCountryByCommKeyProvider);
			encryptedDtoByCommKeyProvider.setSize(encryptedSizeByCommKeyProvider);
			encryptedDtoByCommKeyProvider.setFciGroup(encryptedFciGroupByCommKeyProvider);
			encryptedDtoByCommKeyProvider.setCharacteristics(encryptedCharacteristicsByCommKeyProvider);
			encryptedDtoByCommKeyProvider.setLifeExpectance(encryptedLifeExpByCommKeyProvider);
			encryptedDtoByCommKeyProvider.setImageURL(encryptedImageURLByCommKeyProvider);
			encryptedDtoByCommKeyProvider.setBreedingFarms(encryptedFarms);
			encryptedDtos.add(encryptedDtoByCommKeyProvider);		
		}
		
		return encryptedDtos;
		
	}
	
	
	public BreedDto encryptBreedWithInternalKey(BreedDto b) {
			
			
			
			/* Crypting with our internal key now*/
			String name = b.getName();
			String country = b.getCountry();
			String size = b.getSize();
			String lifeExpectance = b.getLifeExpectance();
			String imageURL = b.getImageURL();
			String fciGroup = b.getFciGroup();
			String characteristics = b.getCharacteristics();		
			Set<BreedingFarmDto> farms = b.getBreedingFarms();
			
			
			/* Effettuo la crittografia con la nostra chiave interna */
			String cryptedName = c.getEncriptedText(name);
			String cryptedCountry = c.getEncriptedText(country);
			String cryptedSize = c.getEncriptedText(size);
			String cryptedLifeExp = c.getEncriptedText(lifeExpectance);
			String cryptedImageURL = c.getEncriptedText(imageURL);
			String cryptedFciGroup = c.getEncriptedText(fciGroup);
			String cryptedCharacteristics = c.getEncriptedText(characteristics);
			/*Encrypting farms*/
			Set<BreedingFarmDto> encryptedFarms = farms.stream().map(farm -> {
				BreedingFarmDto dto = new BreedingFarmDto();
				
				dto.setId(farm.getId());
				dto.setFarmName(c.getEncriptedText(farm.getFarmName()));
				dto.setAddress(c.getEncriptedText(farm.getAddress()));
				dto.setTelephone(c.getEncriptedText(farm.getTelephone()));		
				return dto;			
			}).collect(Collectors.toSet());
			
	
	
			/* DTO encrypted */
			BreedDto encryptedDto = new BreedDto();
			encryptedDto.setId(b.getId());
			encryptedDto.setName(cryptedName);
			encryptedDto.setCountry(cryptedCountry);
			encryptedDto.setSize(cryptedSize);
			encryptedDto.setFciGroup(cryptedFciGroup);
			encryptedDto.setCharacteristics(cryptedCharacteristics);
			encryptedDto.setImageURL(cryptedImageURL);
			encryptedDto.setLifeExpectance(cryptedLifeExp);
			encryptedDto.setBreedingFarms(encryptedFarms);
		
			
			return encryptedDto;
	}
	
	/*Decrypt DTO with our InternalKey*/
	public BreedDto decryptWithInternalKey(BreedDto dto) {
		
		// Decriptaggio dei singoli attributi
		String decryptedName = c.getDecryptedText(dto.getName());
		String decryptedCountry = c.getDecryptedText(dto.getCountry());
		String decryptedSize = c.getDecryptedText(dto.getSize());
		String decryptedLifeExpectance = c.getDecryptedText(dto.getLifeExpectance());
		String decryptedFciGroup = c.getDecryptedText(dto.getFciGroup());
		String decryptedCharacteristics = c.getDecryptedText(dto.getCharacteristics());
		String decryptedImageURL = c.getDecryptedText(dto.getImageURL());
		/*Decrypting farms*/
		Set<BreedingFarmDto> decryptedFarms = dto.getBreedingFarms().stream().map(farm -> {
			BreedingFarmDto breedingFarmDto = new BreedingFarmDto();
			
			breedingFarmDto.setId(farm.getId());
			breedingFarmDto.setFarmName(c.getDecryptedText(farm.getFarmName()));
			breedingFarmDto.setAddress(c.getDecryptedText(farm.getAddress()));
			breedingFarmDto.setTelephone(c.getDecryptedText(farm.getTelephone()));
			
			return breedingFarmDto;
				
		}).collect(Collectors.toSet());
		
		// Nuovo Dto decriptato
		BreedDto decryptedDto = new BreedDto();
		decryptedDto.setId(dto.getId());
		decryptedDto.setName(decryptedName);
		decryptedDto.setCountry(decryptedCountry);
		decryptedDto.setSize(decryptedSize);
		decryptedDto.setCharacteristics(decryptedCharacteristics);
		decryptedDto.setFciGroup(decryptedFciGroup);
		decryptedDto.setImageURL(decryptedImageURL);
		decryptedDto.setLifeExpectance(decryptedLifeExpectance);
		decryptedDto.setBreedingFarms(decryptedFarms);
		
		return decryptedDto;
				
		
	}
		


    /*Returns decrypted List of DTOs with our internalKey */
	public List<BreedDto> decryptBreedsWithOurInternalKey(List<BreedDto> breedDtos){
		
		for (int n=0; n < breedDtos.size(); n++ ) {
			
			Long id = breedDtos.get(n).getId();
			String encryptedName = breedDtos.get(n).getName();
			String encryptedCountry = breedDtos.get(n).getCountry();
			String encryptedSize = breedDtos.get(n).getSize();
			String encryptedLifeExp = breedDtos.get(n).getLifeExpectance();
			String encryptedImageURL = breedDtos.get(n).getImageURL();
			String encryptedFciGroup = breedDtos.get(n).getFciGroup();
			String encryptedCharacteristics = breedDtos.get(n).getCharacteristics();
			Set<BreedingFarmDto> farms = breedDtos.get(n).getBreedingFarms();		
						
			/*Decifro tutte le razze*/
			breedDtos.get(n).setId(id);
			breedDtos.get(n).setName(c.getDecryptedText(encryptedName));
			breedDtos.get(n).setCountry(c.getDecryptedText(encryptedCountry));
			breedDtos.get(n).setSize(c.getDecryptedText(encryptedSize));
			breedDtos.get(n).setFciGroup(c.getDecryptedText(encryptedFciGroup));
			breedDtos.get(n).setCharacteristics(c.getDecryptedText(encryptedCharacteristics));
			breedDtos.get(n).setImageURL(c.getDecryptedText(encryptedImageURL));
			breedDtos.get(n).setLifeExpectance(c.getDecryptedText(encryptedLifeExp));
			
			Set<BreedingFarmDto> decryptedFarms = farms.stream().map(farm -> {
				BreedingFarmDto dto = new BreedingFarmDto();
				
				dto.setId(farm.getId());
				dto.setFarmName(c.getDecryptedText(farm.getFarmName()));
				dto.setAddress(c.getDecryptedText(farm.getAddress()));
				dto.setTelephone(c.getDecryptedText(farm.getTelephone()));		
				return dto;			
			}).collect(Collectors.toSet());
			
			breedDtos.get(n).setBreedingFarms(decryptedFarms);			
			
		}
				
			return breedDtos;
		
		
	}
		
	public BreedDto addBreedingFarmToBreed(BreedDto breedDto, Long idFarm, String provUsername) {
		
		
		
		BreedingFarm breedingFarm = breedingFarmDao.findById(idFarm);
			
		Long id = breedDto.getId();
			
		Breed breedToBeUpdated = breedDao.findById(id);
			
		breedToBeUpdated.addBreedingFarm(breedingFarm);
				
		BreedDto breedDtoFinal = breedMapper.toDto(breedToBeUpdated);
			
		BreedDto updated = this.updateBreed(breedDtoFinal, provUsername);
			
		return updated;
					
	}
	

}
