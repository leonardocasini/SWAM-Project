package provider;

import java.util.List;


import breed.BreedDto;
import breed.BreedingFarmDto;

public class MyApplication {
	
    public static void main(String[] args) throws Exception {
    	
    	
    	/* PROVIDER 1 CONFIGURATION */ 

    	/* 0. Create class Provider */
    	String providerName = "ProviderProva";
    	Provider p1 = new Provider(providerName);
    	
    	/* 1. Try to read my public & private keys,  if no keys found, generate and save */
    	p1.loadAsimmetricKeys();
    		
    	/* 2. Load Asymmetric Cipher with keys found */
    	p1.loadAsymmetricCipher();
    	
    	/* 3. Get commKey by provName, if not exists will be created*/
    	if(p1.getCommKeyByProvName()) {
    		
    		System.out.println("Provider decripted key: " + p1.getCommKeyProvider() );
    		
    	}else {
    		
    		System.out.println("CommKey doesn't exist, will be created");
    		
    		p1.createCommKey();
    		
    		/*Once created, do get again */
    		p1.getCommKeyByProvName();
    		System.out.println("CommKey created");
    	}
    	
    	/* 4. Load simmetric cipher with commKey*/
    	System.out.println("Loading cipher...");
    	p1.loadSimmetricCipher();
    	System.out.println("Cypher loaded");
    	
    	
    	/* PROVIDER 2 CONFIGURATION */ 

    	/* 0. Create class Provider */
    	String secondProviderName = "ProviderProva2";
    	Provider p2 = new Provider(secondProviderName);
    	
    	/* 1. Try to read my public & private keys,  if no keys found, generate and save */
    	p2.loadAsimmetricKeys();
    		
    	/* 2. Load Asymmetric Cipher with keys found */
    	p2.loadAsymmetricCipher();
    	
    	/* 3. Get commKey by provName, if not exists will be created*/
    	if(p2.getCommKeyByProvName()) {
    		
    		System.out.println("Provider decripted key: " + p2.getCommKeyProvider() );
    		
    	}else {
    		
    		System.out.println("CommKey doesn't exist, will be created");
    		
    		p2.createCommKey();
    		
    		/*Once created, do get again */
    		p2.getCommKeyByProvName();
    		System.out.println("CommKey created");
    	}
    	
    	/* PROVIDER 3 CONFIGURATION */ 

    	/* 0. Create class Provider */
    	String thirdProviderName = "ProviderProva3";
    	Provider p3 = new Provider(thirdProviderName);
    	
    	/* 1. Try to read my public & private keys,  if no keys found, generate and save */
    	p3.loadAsimmetricKeys();
    		
    	/* 2. Load Asymmetric Cipher with keys found */
    	p3.loadAsymmetricCipher();
    	
    	/* 3. Get commKey by provName, if not exists will be created*/
    	if(p3.getCommKeyByProvName()) {
    		
    		System.out.println("Provider decripted key: " + p3.getCommKeyProvider() );
    		
    	}else {
    		
    		System.out.println("CommKey doesn't exist, will be created");
    		
    		p3.createCommKey();
    		
    		/*Once created, do get again */
    		p3.getCommKeyByProvName();
    		System.out.println("CommKey created");
    	}
    	
    	
    	/* 4. Load simmetric cipher with commKey*/
    	System.out.println("Loading cipher...");
    	p3.loadSimmetricCipher();
    	System.out.println("Cypher loaded");
    	
    	/*****************************************************************************************************/
    	
    	/*Once I have my commKey I can use CRUD operations*/
    	
    	
    	/*PROVIDER 1 DOES OPERATIONS*/
    	
    	/* Populating our Database */
    	/* 4.1. CREAZIONE RAZZA (name, country, size, lifeExpectance, characteristics, imageURL, fciGroup);  */
    	
    	p1.createBreed("Bassotto", "Italia", "Grande", "12Y", "Docile", "sitoWebProvider.com", "1");
    	p1.createBreed("Bulldog", "Francia", "Piccolo", "12Y", "Amichevole", "sitoWebProvider2.com", "2");
    	p1.createBreed("Rottweiler", "Germania", "Grande", "15Y", "Aggressivo", "sitoWebProvider3.com", "3");
    	
    	/*CREATING BREEDING FARM*/
    	
    	p1.createBreedingFarm("PrimoFarm", "Montevarchi", "05583736");
    	p1.createBreedingFarm("SecondoFarm", "Terranuova", "055836736");
    	
    	
    	/*PROVIDER 2 DOES OPERATIONS*/
    	
    	/* Populating our Database */
    	/* 4.1. CREAZIONE RAZZA (name, country, size, lifeExpectance, characteristics, imageURL, fciGroup);  */
    	
    	p2.createBreed("Pitbull", "Italia", "Medio", "13Y", "Docile", "sitoWebProvider.com", "1");

    	
    	/*PROVIDER 3 DOES OPERATIONS*/
    	
    	/*CREATING BREEDING FARM*/
    	
    	p3.createBreedingFarm("TerzoFarm", "Rifredi", "0554483736");
    	
    	/* NOW P3 GET ID OF BASSOTTO AND PRIMOFARM TO DO THE ASSIGNMENT*/
    	/* GET by name */
    	String breedTarget2 = "Bassotto";
    	BreedDto breedDto2 = p3.getBreedByName(breedTarget2);
    	if(breedDto2 != null) {
    		p3.printBreedFound(breedDto2);
    	}
    	
    	/* 4.2. GET by name BREEDINGFARM */
    	String breedingFarmTarget = "TerzoFarm";
    	BreedingFarmDto farm = p3.getFarmByName(breedingFarmTarget);
    	if(farm != null) {
    		p3.printBreedingFarmFound(farm);
    	}
    	
    	
    	/*ASSIGNING BREEDINGFARM TO BREED*/
    	p3.addBreedingFarmInBreed(
    			breedDto2.getId(), 
    			breedDto2.getName(), 
    			breedDto2.getCountry(), 
    			breedDto2.getSize(), 
    			breedDto2.getLifeExpectance(), 
    			breedDto2.getCharacteristics(), 
    			breedDto2.getImageURL(), 
    			breedDto2.getFciGroup(), 
    			farm.getId());

    	
    	
    	/*PROVIDER 1 GET ALL*/
    	
    	
    	/*GET FARMS OF A BREED*/
	   	 List<BreedingFarmDto> farms = p1.farmsForBreed(4L);
	   	 
	   	 p1.printFarms(farms);
	   	 
    	/* GET all BREEDS */   	
    	List<BreedDto> breedDtos = p1.getAllBreeds();
    	
    	if( breedDtos != null) {
    		p1.printBreeds(breedDtos);      	
    	}
    	
    	
    	
    	
    	
    
  	 
    	 
    	 
    	 
    	

    	
    }
    
    
}

 
    

    
    

