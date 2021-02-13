package provider;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import assimetric.AsymmetricCryptography;
import assimetric.GeneratorKeys;
import breed.BreedDto;
import breed.BreedingFarmDto;
import breed.CommKeyDto;
import simmetric.SimmetricCryptography;

import org.json.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.http.HttpEntity;

public class Provider {
	private String PUBLIC_KEY; // = "provUsername/public.key";
	private String PRIVATE_KEY; //  = "provUsername/private.key";
	private String provUsername; 
	private File publicKeyFile;
	private File privateKeyFile;
	private String commKeyProvider;
	private AsymmetricCryptography assimCipher;
	private SimmetricCryptography simmCipher;
	private Gson g = new Gson();
	
	// Our ENDPOINT
	private final static String API_ENDPOINT = "http://localhost:8080/dogs-breeds-backend/api/";
	
	public Provider(String name) {
		provUsername = name;
		commKeyProvider = null;
		simmCipher = null;
	}
	
	public String getCommKeyProvider() {
		return commKeyProvider;
	}
	
	public void loadAsimmetricKeys() {
		String publicKeyPath = provUsername + "/public.key";
		String privateKeyPath = provUsername + "/private.key";
		publicKeyFile = new File(publicKeyPath); //PUBLIC_KEY
    	privateKeyFile = new File(privateKeyPath); //PRIVATE_KEY
		if( !publicKeyFile.exists()  || !privateKeyFile.exists()) { // Check if files exist or not
		    		
		    		//If not exists
		        	GeneratorKeys generator = new GeneratorKeys(512); 
		        	/* This method generate the keys and save them in those paths*/
		        	generator.saveKeys(publicKeyPath, privateKeyPath); 
		        	
		        	System.out.println("PUB & PRIV KEYS GENERATED");
		        	
		        	publicKeyFile = new File(publicKeyPath);
		        	privateKeyFile = new File(privateKeyPath);
		        	
		    	} else {
		    		
		    		System.out.println("KEYS FOUND");
		    		
		    	}
	}
	
	public void loadAsymmetricCipher() {
		assimCipher = new AsymmetricCryptography();
    	
    	try {
    		assimCipher.loadPublicKey(publicKeyFile.getCanonicalPath());//  Load public key by path 
    		assimCipher.loadPrivateKey(privateKeyFile.getCanonicalPath());// Load private key by path 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	 
	}
	
	public boolean getCommKeyByProvName() {
		// Get that returns CommKey Value by provUsername
    	CommKeyDto commKey = sendGETRequest( API_ENDPOINT + "commKeys/provUsername/" + provUsername, CommKeyDto.class);
    	    	
    	if (commKey != null) {	
        	commKeyProvider = assimCipher.decryptText(commKey.getValue()); //Decrypt value with PrivKey
        	return true;
        	//return commKeyProvider;
       
    		
    	}else {
    		
    		//return null;
    		return false;
    		//If not exists create into Server POST
    		        	
    	}
	}
	
	public void createCommKey() {
		//Costruzione jsonString per saveKey
		String jsonInputString = "{\"provUsername\": \""+ provUsername +"\"}";
    	sendPOSTRequest( API_ENDPOINT + "commKeys",jsonInputString);
	}
	
	public void loadSimmetricCipher() {
		if(commKeyProvider!=null) {
			simmCipher = new SimmetricCryptography(commKeyProvider);
			
		}else {
			System.out.println("Ricerca la chiave di comunicazione prima di precedere " );
		}
	}
		
	public void createBreed(String name, String country, String size, String lifeExpectance,
			String characteristics, String imageURL, String fciGroup) {
		if(simmCipher!=null) {
			/*Cifro la breed coN la simmetricKey per evitare il testo in chiaro */
			String encryptedName = simmCipher.getEncriptedText(name);
			String encryptedCountry = simmCipher.getEncriptedText(country);
			String encryptedSize = simmCipher.getEncriptedText(size);
			String encryptedLifeExpectance = simmCipher.getEncriptedText(lifeExpectance);
			String encryptedCharacteristics = simmCipher.getEncriptedText(characteristics);
			String encryptedImageURL = simmCipher.getEncriptedText(imageURL);
			String encryptedFciGroup = simmCipher.getEncriptedText(fciGroup);
			
			// Create json for breed crifrato
	    	String jsonInputStringForCreateBreed =
	    			"{\"name\": \""+ encryptedName +"\","
	    			+ " \"country\": \""+encryptedCountry+"\","
	    			+ " \"size\": \""+encryptedSize+"\","
	    			+ " \"lifeExpectance\": \""+encryptedLifeExpectance+"\","
	    			+ " \"characteristics\": \""+encryptedCharacteristics+"\","
	    			+ " \"imageURL\": \""+encryptedImageURL+"\","
	    			+ " \"fciGroup\": \""+encryptedFciGroup+"\""	
	    			+ "}";
			
	    	sendPOSTRequest( API_ENDPOINT + "breeds?provUsername=" + provUsername ,jsonInputStringForCreateBreed);
		}else {
			System.out.println("Carica il cifratore simmetrico prima di precedere " );
		}
		
		
	}
		
	public List<BreedDto> getAllBreeds() {
		if(simmCipher!=null) {
			
			ArrayList<BreedDto> breeds = sendGETRequestArray( API_ENDPOINT + "breeds" + "?provUsername=" +  provUsername , BreedDto.class);

        	List<BreedDto> breedDtos = new ArrayList<>();
	    				
				for (int i = 0; i < breeds.size(); i++ ) {
					
					BreedDto breedFound = g.fromJson(g.toJson(breeds.get(i)), BreedDto.class);
					
		        	String breedDecryptedName = simmCipher.getDecryptedText(breedFound.getName()); // Decrypt value with PrivKey
		        	String breedDecryptedSize = simmCipher.getDecryptedText(breedFound.getSize());
		        	String breedDecryptedCountry = simmCipher.getDecryptedText(breedFound.getCountry());
		        	String breedDecryptedFciGroup = simmCipher.getDecryptedText(breedFound.getFciGroup());
		        	String breedDecryptedCharacteristics = simmCipher.getDecryptedText(breedFound.getCharacteristics());
		        	String breedDecryptedImageURL = simmCipher.getDecryptedText(breedFound.getImageURL());
		        	String breedDecryptedLifeExpectance = simmCipher.getDecryptedText(breedFound.getLifeExpectance());
		        	
		 
		        	
		        	BreedDto breedDto =  new BreedDto();
		        	
		        	breedDto.setId(breedFound.getId());
		        	breedDto.setName(breedDecryptedName);
		        	breedDto.setSize(breedDecryptedSize);
		        	breedDto.setCharacteristics(breedDecryptedCharacteristics);
		        	breedDto.setFciGroup(breedDecryptedFciGroup);
		        	breedDto.setImageURL(breedDecryptedImageURL);
		        	breedDto.setLifeExpectance(breedDecryptedLifeExpectance);
		        	breedDto.setCountry(breedDecryptedCountry);
		        	
		        	breedDtos.add(breedDto);		        			        			        	
				}
				
				return breedDtos;
	 		
		}else {
			System.out.println("Carica il cifratore simmetrico prima di procedere");
			return null;
		}
		
		
	}

	public BreedDto getBreedByName(String breedNameTarget) {
		if(simmCipher!=null) {
			BreedDto breedByName = sendGETRequest( API_ENDPOINT + "breeds/name/" + breedNameTarget + "?provUsername=" +  provUsername ,
					BreedDto.class);
			if (breedByName != null) {
	    		   	    		
	        	String breedDecryptedName = simmCipher.getDecryptedText(breedByName.getName()); // Decrypt value with PrivKey
	        	String breedDecryptedSize = simmCipher.getDecryptedText(breedByName.getSize());
	        	String breedDecryptedCountry = simmCipher.getDecryptedText(breedByName.getCountry());
	        	String breedDecryptedFciGroup = simmCipher.getDecryptedText(breedByName.getFciGroup());
	        	String breedDecryptedCharacteristics = simmCipher.getDecryptedText(breedByName.getCharacteristics());
	        	String breedDecryptedImageURL = simmCipher.getDecryptedText(breedByName.getImageURL());
	        	String breedDecryptedLifeExpectance = simmCipher.getDecryptedText(breedByName.getLifeExpectance());
	        	
	        	BreedDto breedFound = new BreedDto();
	        	
	        	breedFound.setId(breedByName.getId());
	        	breedFound.setName(breedDecryptedName);
	        	breedFound.setSize(breedDecryptedSize);
	        	breedFound.setCharacteristics(breedDecryptedCharacteristics);
	        	breedFound.setFciGroup(breedDecryptedFciGroup);
	        	breedFound.setImageURL(breedDecryptedImageURL);
	        	breedFound.setLifeExpectance(breedDecryptedLifeExpectance);
	        	breedFound.setCountry(breedDecryptedCountry);
	        	
	        	
	        	return breedFound;
	        		    		
	    	}else {
	    		System.out.println("BREED NOT FOUND ");
	    		return null;
	    	}
			
		}else {
			System.out.println("Carica il cifratore simmetrico prima di precedere");
			return null;
		}				
	}
	
	public BreedingFarmDto getFarmByName(String farmTarget) {
		if(simmCipher!=null) {
			BreedingFarmDto farmByName = sendGETRequest( API_ENDPOINT + "farms/farmName/" + farmTarget + "?provUsername=" +  provUsername ,
					BreedingFarmDto.class);
			if (farmByName != null) {
	    		   	    		
	        	String breedingFarmDecryptedName = simmCipher.getDecryptedText(farmByName.getFarmName()); // Decrypt value with PrivKey
	        	String breedingFarmDecryptedAddress = simmCipher.getDecryptedText(farmByName.getAddress());
	        	String breedingFarmDecryptedTelephone = simmCipher.getDecryptedText(farmByName.getTelephone());
	        	
	        	
	        	BreedingFarmDto farmFound = new BreedingFarmDto();
	        	
	        	farmFound.setId(farmByName.getId());
	        	farmFound.setFarmName(breedingFarmDecryptedName);
	        	farmFound.setTelephone(breedingFarmDecryptedTelephone);
	        	farmFound.setAddress(breedingFarmDecryptedAddress);
	        	
	        	
	        	return farmFound;
	        		    		
	    	}else {
	    		System.out.println("BREEDING FARM NOT FOUND ");
	    		return null;
	    	}
			
		}else {
			System.out.println("Carica il cifratore simmetrico prima di precedere");
			return null;
		}		
	}
	
	
	
		
	public List<BreedDto> breedByCountry(String breedCountryTarget) {
		if(simmCipher!=null) {
			
			ArrayList<BreedDto> breeds = sendGETRequestArray( API_ENDPOINT + "breeds/country/" + breedCountryTarget + "?provUsername=" +  provUsername  , BreedDto.class);

        	List<BreedDto> breedDtos = new ArrayList<>();
	    				
				for (int i = 0; i < breeds.size(); i++ ) {
					
					BreedDto breedFound = g.fromJson(g.toJson(breeds.get(i)), BreedDto.class);
					
		        	String breedDecryptedName = simmCipher.getDecryptedText(breedFound.getName()); // Decrypt value with PrivKey
		        	String breedDecryptedSize = simmCipher.getDecryptedText(breedFound.getSize());
		        	String breedDecryptedCountry = simmCipher.getDecryptedText(breedFound.getCountry());
		        	String breedDecryptedFciGroup = simmCipher.getDecryptedText(breedFound.getFciGroup());
		        	String breedDecryptedCharacteristics = simmCipher.getDecryptedText(breedFound.getCharacteristics());
		        	String breedDecryptedImageURL = simmCipher.getDecryptedText(breedFound.getImageURL());
		        	String breedDecryptedLifeExpectance = simmCipher.getDecryptedText(breedFound.getLifeExpectance());
		        	
		 
		        	
		        	BreedDto breedDto =  new BreedDto();
		        	
		        	breedDto.setId(breedFound.getId());
		        	breedDto.setName(breedDecryptedName);
		        	breedDto.setSize(breedDecryptedSize);
		        	breedDto.setCharacteristics(breedDecryptedCharacteristics);
		        	breedDto.setFciGroup(breedDecryptedFciGroup);
		        	breedDto.setImageURL(breedDecryptedImageURL);
		        	breedDto.setLifeExpectance(breedDecryptedLifeExpectance);
		        	breedDto.setCountry(breedDecryptedCountry);
		        	
		        	breedDtos.add(breedDto);		        			        			        	
				}
				
				return breedDtos;
	 		
		}else {
			System.out.println("Carica il cifratore simmetrico prima di procedere");
			return null;
		}
		
		
	}
	
	public List<BreedingFarmDto> farmsForBreed(Long id) {
		
		if(simmCipher!=null) {
			
			ArrayList<BreedingFarmDto> farms = sendGETRequestArray( API_ENDPOINT + "breeds/idBreed/" + id + "?provUsername=" +  provUsername  , BreedingFarmDto.class);
			ArrayList<BreedingFarmDto> newfarms = new ArrayList<>();
			
			for(int i = 0 ; i < farms.size(); i++) {
				
				
				
				BreedingFarmDto farmFound = g.fromJson(g.toJson(farms.get(i)), BreedingFarmDto.class);
				
				Long farmId = farmFound.getId();
				String name = farmFound.getFarmName();
				String address = farmFound.getAddress();
				String telephone = farmFound.getTelephone();
				
				BreedingFarmDto decryptedDto= new BreedingFarmDto();
				
				String decryptedName = simmCipher.getDecryptedText(name);
				String decryptedAddress = simmCipher.getDecryptedText(address);
				String decryptedTelephone = simmCipher.getDecryptedText(telephone);
				
				decryptedDto.setId(farmId);
				decryptedDto.setFarmName(decryptedName);
				decryptedDto.setAddress(decryptedAddress);
				decryptedDto.setTelephone(decryptedTelephone);
				
				
				newfarms.add(decryptedDto);
			}
			
			
			return newfarms;
	 		
		}else {
			System.out.println("Carica il cifratore simmetrico prima di procedere");
			return null;
		}
		
		
	}
	
	public void printFarms(List<BreedingFarmDto> farms) {
		
		for (int i = 0 ; i < farms.size(); i++) {		
			System.out.println(
					"Lista di farms della razza" +
					"ID: " + farms.get(i).getId() + ", " +
					"NAME: " + farms.get(i).getFarmName()+", ADDRESS: " + farms.get(i).getAddress()+
					", TELEPHONE "+ farms.get(i).getTelephone());
		}
		
		
		
	}
	
	public List<BreedDto> breedBySize(String breedSizeTarget) {
		if(simmCipher!=null) {
			
			ArrayList<BreedDto> breeds = sendGETRequestArray( API_ENDPOINT + "breeds/size/" + breedSizeTarget + "?provUsername=" +  provUsername  , BreedDto.class);

        	List<BreedDto> breedDtos = new ArrayList<>();
	    				
				for (int i = 0; i < breeds.size(); i++ ) {
					
					BreedDto breedFound = g.fromJson(g.toJson(breeds.get(i)), BreedDto.class);
					
		        	String breedDecryptedName = simmCipher.getDecryptedText(breedFound.getName()); // Decrypt value with PrivKey
		        	String breedDecryptedSize = simmCipher.getDecryptedText(breedFound.getSize());
		        	String breedDecryptedCountry = simmCipher.getDecryptedText(breedFound.getCountry());
		        	String breedDecryptedFciGroup = simmCipher.getDecryptedText(breedFound.getFciGroup());
		        	String breedDecryptedCharacteristics = simmCipher.getDecryptedText(breedFound.getCharacteristics());
		        	String breedDecryptedImageURL = simmCipher.getDecryptedText(breedFound.getImageURL());
		        	String breedDecryptedLifeExpectance = simmCipher.getDecryptedText(breedFound.getLifeExpectance());
		        	
		 
		        	
		        	BreedDto breedDto =  new BreedDto();
		        	
		        	breedDto.setId(breedFound.getId());
		        	breedDto.setName(breedDecryptedName);
		        	breedDto.setSize(breedDecryptedSize);
		        	breedDto.setCharacteristics(breedDecryptedCharacteristics);
		        	breedDto.setFciGroup(breedDecryptedFciGroup);
		        	breedDto.setImageURL(breedDecryptedImageURL);
		        	breedDto.setLifeExpectance(breedDecryptedLifeExpectance);
		        	breedDto.setCountry(breedDecryptedCountry);
		        	
		        	breedDtos.add(breedDto);		        			        			        	
				}
				
				return breedDtos;
	 		
		}else {
			System.out.println("Carica il cifratore simmetrico prima di procedere");
			return null;
		}
		
		
	}
	
	
	public void createBreedingFarm(String farmName, String address, String telephone) {
		if(simmCipher!=null) {
			/*Cifro la breed con la simmetricKey per evitare il testo in chiaro */
			String encryptedFarmName = simmCipher.getEncriptedText(farmName);
			String encryptedAddress = simmCipher.getEncriptedText(address);
			String encryptedTelephone = simmCipher.getEncriptedText(telephone);
			
			
			// Create json for breed cifrato
	    	String jsonInputStringForCreateBreedingfarm =
	    			"{\"farmName\": \""+ encryptedFarmName +"\","
	    			+ " \"address\": \""+encryptedAddress+"\","
	    			+ " \"telephone\": \""+encryptedTelephone+"\""		
	    			+ "}";
			
	    	sendPOSTRequest( API_ENDPOINT + "farms?provUsername=" + provUsername ,jsonInputStringForCreateBreedingfarm);
		}else {
			System.out.println("Carica il cifratore simmetrico prima di precedere " );
		}		
	}
	
	public void addBreedingFarmInBreed(Long id, String name, String country, String size, String lifeExpectance,
			String characteristics, String imageURL, String fciGroup, Long idFarm) {
		if(simmCipher!=null) {
			
			
			/*Cifro la breed coN la simmetricKey per evitare il testo in chiaro */
			String encryptedName = simmCipher.getEncriptedText(name);
			String encryptedCountry = simmCipher.getEncriptedText(country);
			String encryptedSize = simmCipher.getEncriptedText(size);
			String encryptedLifeExpectance = simmCipher.getEncriptedText(lifeExpectance);
			String encryptedCharacteristics = simmCipher.getEncriptedText(characteristics);
			String encryptedImageURL = simmCipher.getEncriptedText(imageURL);
			String encryptedFciGroup = simmCipher.getEncriptedText(fciGroup);
			
			// Create json for breed crifrato
	    	String jsonInputStringForAddingBreeding =
	    			"{\"name\": \""+ encryptedName +"\","
	    			+ " \"id\": \""+id+"\","
	    			+ " \"country\": \""+encryptedCountry+"\","
	    			+ " \"size\": \""+encryptedSize+"\","
	    			+ " \"lifeExpectance\": \""+encryptedLifeExpectance+"\","
	    			+ " \"characteristics\": \""+encryptedCharacteristics+"\","
	    			+ " \"imageURL\": \""+encryptedImageURL+"\","
	    			+ " \"fciGroup\": \""+encryptedFciGroup+"\""	
	    			+ "}";
			
			sendPOSTRequest( API_ENDPOINT + "farms/" + idFarm + "/addBreed" + "?provUsername=" +  provUsername  , jsonInputStringForAddingBreeding);       	        			        			  
	 		
		}else {
			System.out.println("Carica il cifratore simmetrico prima di procedere");
			
		}
		
	}
		
	public void printBreedFound(BreedDto b) {	
		System.out.println(
				"Razza cercata:" +
				"ID: " + b.getId() + ", " +
				"NAME: " + b.getName()+", SIZE: " + b.getSize()+
				", FCI_GROUP: "+b.getFciGroup()+", IMAGE_URL: "+b.getImageURL()+
				", CHARACTERISTICS: "+b.getCharacteristics()+", LIFE_EXPECTANCE: "+b.getLifeExpectance()+
				", COUNTRY: "+b.getCountry());		
	}
	
	public void printBreedingFarmFound(BreedingFarmDto b) {	
		System.out.println(
				"Farm cercata:" +
				"ID: " + b.getId() + ", " +
				"NAME: " + b.getFarmName()+", ADDRESS: " + b.getAddress()+
				", TELEPHONE: "+b.getTelephone());		
	}
	
		
	public void printBreeds(List <BreedDto> breeds) {
		
		for (int i = 0 ; i < breeds.size(); i++) {		
			System.out.println(
					"Lista di razze" +
					"ID: " + breeds.get(i).getId() + ", " +
					"NAME: " + breeds.get(i).getName()+", SIZE: " + breeds.get(i).getSize()+
					", FCI_GROUP: "+ breeds.get(i).getFciGroup()+", IMAGE_URL: "+ breeds.get(i).getImageURL()+
					", CHARACTERISTICS: "+ breeds.get(i).getCharacteristics()+", LIFE_EXPECTANCE: "+ breeds.get(i).getLifeExpectance()+
					", COUNTRY: "+ breeds.get(i).getCountry());
		}				
	}
	
	public void updateBreed(Long id, String name, String country, String size, String lifeExpectance,
			String characteristics, String imageURL, String fciGroup) {
		if(simmCipher!=null) {
			/*Cifro la breed con la simmetricKey per evitare il testo in chiaro */
			String encryptedName = simmCipher.getEncriptedText(name);
			String encryptedCountry = simmCipher.getEncriptedText(country);
			String encryptedSize = simmCipher.getEncriptedText(size);
			String encryptedLifeExpectance = simmCipher.getEncriptedText(lifeExpectance);
			String encryptedCharacteristics = simmCipher.getEncriptedText(characteristics);
			String encryptedImageURL = simmCipher.getEncriptedText(imageURL);
			String encryptedFciGroup = simmCipher.getEncriptedText(fciGroup);
			
			// Create json for breed crifrato
	    	String jsonInputStringForUpdateBreed =
	    			"{\"id\": \""+ id +"\","
	    			+ "\"name\": \""+ encryptedName +"\","
	    			+ " \"country\": \""+encryptedCountry+"\","
	    			+ " \"size\": \""+encryptedSize+"\","
	    			+ " \"lifeExpectance\": \""+encryptedLifeExpectance+"\","
	    			+ " \"characteristics\": \""+encryptedCharacteristics+"\","
	    			+ " \"imageURL\": \""+encryptedImageURL+"\","
	    			+ " \"fciGroup\": \""+encryptedFciGroup+"\""	
	    			+ "}";
			
	    	sendPUTRequest( API_ENDPOINT + "breeds?provUsername=" + provUsername ,jsonInputStringForUpdateBreed);
		}else {
			System.out.println("Carica il cifratore simmetrico prima di precedere " );
		}
		
		
	}
	
	public void deleteBreed(String breedId) {

		if(simmCipher!=null) {
		sendDELETERequest( API_ENDPOINT + "breeds/" + breedId);
		}else {
			System.out.println("Carica il cifratore simmetrico prima di precedere " );	
		}		
	}
	
	
	/*GENERIC HTTP METHODS*/
	
	
	public Object sendPOSTRequest(String path, String jsonInputString) { 
    	URL url;
		try {
			url = new URL(path);
			URLConnection con = url.openConnection();
	    	HttpURLConnection http = (HttpURLConnection)con;
	    	http.setRequestMethod("POST");
	    	http.setRequestProperty("Content-Type", "application/json; UTF-8");
	    	http.setRequestProperty("Accept", "application/json");
	    	http.setDoOutput(true);
	    	
	    	
	    	try(OutputStream os = http.getOutputStream()) {
	    	    byte[] input = jsonInputString.getBytes("utf-8");
	    	    os.write(input, 0, input.length);			
	    	}
	    	int code = http.getResponseCode();
			System.out.println(code);
	    	try(BufferedReader br = new BufferedReader(
	    			  new InputStreamReader(http.getInputStream(), "utf-8"))) {
	    			    StringBuilder response = new StringBuilder();
	    			    String responseLine = null;
	    			    while ((responseLine = br.readLine()) != null) {
	    			        response.append(responseLine.trim());
	    			    }
	    			    System.out.println(response.toString());
	    			}
	    	
    		return null;

		} catch( Exception e ) {
    		return null;
    		}
    	
    }
	
	public  <T extends Object> T sendGETRequest(String path, Class<T> c) {
	      
    	try {
    		URL url = new URL(path);
	    	URLConnection con = url.openConnection();
	    	HttpURLConnection http = (HttpURLConnection)con;
	    	
	    	http.setRequestProperty("RSAkey", publicKeyFile.getAbsolutePath());
	    	http.setRequestMethod("GET"); 
	    	http.setDoOutput(true);

	    	int responseCode = http.getResponseCode();
			System.out.println("GET Response Code :: " + responseCode);
			
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();
	
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine+ '\n');
				}
				in.close();
				
				
				return this.g.fromJson(response.toString(), c);
				
			} else throw new Exception();
			
    	} catch( Exception e ) {
    		return null;
    		}
    }

	public  <T extends Object> ArrayList<T> sendGETRequestArray(String path, Class<T> c) {
	      
		try {
			URL url = new URL(path);
	    	URLConnection con = url.openConnection();
	    	HttpURLConnection http = (HttpURLConnection)con;
	    	
	    	http.setRequestProperty("RSAkey", publicKeyFile.getAbsolutePath());
	    	http.setRequestMethod("GET"); 
	    	http.setDoOutput(true);
	
	    	int responseCode = http.getResponseCode();
			System.out.println("GET Response Code :: " + responseCode);
			
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();
	
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine+ '\n');
				}
				in.close();
				
				
				return (ArrayList<T>)this.g.fromJson(response.toString(), ArrayList.class);
				
			} else throw new Exception();
			
		} catch( Exception e ) {
			return null;
			}
	}

	public Object sendPUTRequest(String path, String jsonInputString) { 
    	URL url;
		try {
			url = new URL(path);
			URLConnection con = url.openConnection();
	    	HttpURLConnection http = (HttpURLConnection)con;
	    	http.setRequestMethod("PUT");
	    	http.setRequestProperty("Content-Type", "application/json; utf-8");
	    	http.setRequestProperty("Accept", "application/json");
	    	http.setDoOutput(true);
	    	
	    	
	    	try(OutputStream os = http.getOutputStream()) {
	    	    byte[] input = jsonInputString.getBytes("utf-8");
	    	    os.write(input, 0, input.length);			
	    	}
	    	int code = http.getResponseCode();
			System.out.println(code);
	    	try(BufferedReader br = new BufferedReader(
	    			  new InputStreamReader(http.getInputStream(), "utf-8"))) {
	    			    StringBuilder response = new StringBuilder();
	    			    String responseLine = null;
	    			    while ((responseLine = br.readLine()) != null) {
	    			        response.append(responseLine.trim());
	    			    }
	    			    System.out.println(response.toString());
	    			}
	    	
    		return null;

		} catch( Exception e ) {
    		return null;
    		}
    	
    }
	
	public Object sendDELETERequest(String path) {
	      
    	try {
    		URL url = new URL(path);
	    	URLConnection con = url.openConnection();
	    	HttpURLConnection http = (HttpURLConnection)con;
	    	
	    	http.setRequestProperty("RSAkey", publicKeyFile.getAbsolutePath());
	    	http.setRequestMethod("DELETE"); 
	    	http.setDoOutput(true);

	    	int responseCode = http.getResponseCode();
			System.out.println("GET Response Code :: " + responseCode);
			
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();
	
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine+ '\n');
				}
				in.close();
				
				
				return response.toString();
				
			} else throw new Exception();
			
    	} catch( Exception e ) {
    		return null;
    		}
    }
	
	
	
	
}