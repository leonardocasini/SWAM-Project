


/*Now it's all done by client's project*/

//package it.unifi.dinfo.stlab.dogs_breeds_backend;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLConnection;
//
//import assimetric.AsymmetricCryptography;
//import assimetric.GeneratorKeys;
//import it.unifi.dinfo.stlab.dogs_breeds_backend.dto.BreedDto;
//import it.unifi.dinfo.stlab.dogs_breeds_backend.dto.CommKeyDto;
//import simmetric.SimmetricCryptography;
//import com.google.gson.Gson;
//
//
//public class Provider {
//	private String PUBLIC_KEY; // = "adel/public.key";
//	private String PRIVATE_KEY; //  = "adel/private.key";
//	private String provUsername; // "secondoProvider"
//	private File publicKeyFile;
//	private File privateKeyFile;
//	private String commKeyProvider;
//	private AsymmetricCryptography assimCipher;
//	private SimmetricCryptography simmCipher;
//	private Gson g = new Gson();
//	
//	// Our ENDPOINT
//	private final static String API_ENDPOINT = "http://localhost:8080/dogs-breeds-backend/api/";
//	
//	public Provider(String name) {
//		provUsername = name;
//		commKeyProvider = null;
//		simmCipher = null;
//	}
//	
//	public void generateKeys(String publicKeyPath, String privateKeyPath ) {
//		publicKeyFile = new File(publicKeyPath); //PUBLIC_KEY
//    	privateKeyFile = new File(privateKeyPath); //PRIVATE_KEY
//		if( !publicKeyFile.exists()  || !privateKeyFile.exists()) { // Check if files exist or not
//		    		
//		    		//If not exists
//		        	GeneratorKeys generator = new GeneratorKeys(512); 
//		        	/* This method generate the keys and save them in those paths*/
//		        	generator.saveKeys(PUBLIC_KEY, PRIVATE_KEY); 
//		        	
//		        	System.out.println("PUB & PRIV KEYS GENERATED");
//		        	
//		        	publicKeyFile = new File(PUBLIC_KEY);
//		        	privateKeyFile = new File(PRIVATE_KEY);
//		        	
//		    	} else {
//		    		
//		    		System.out.println("KEYS FOUND");
//		    		
//		    	}
//	}
//	
//	public void loadAsymmetricCipher() {
//		assimCipher = new AsymmetricCryptography();
//    	
//    	try {
//    		assimCipher.loadPublicKey(publicKeyFile.getCanonicalPath());//  Load public key by path 
//    		assimCipher.loadPrivateKey(privateKeyFile.getCanonicalPath());// Load private key by path 
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//    	 
//	}
//	
//	public void getCommKeyByProvName() {
//		// Get that returns CommKey Value by provUsername
//    	CommKeyDto commKey = sendGETRequest( API_ENDPOINT + "commKeys/provUsername/" + provUsername, CommKeyDto.class);
//    	    	
//    	if (commKey != null) {
//    		System.out.println("Provider key : " + commKey);    		
//            	
//        	commKeyProvider = assimCipher.decryptText(commKey.getValue()); //Decrypt value with PrivKey
//        	
//        	
//        	System.out.println("Provider decripted key: " + commKeyProvider );
//    		
//    	}else {
//    		//If not exists create into Server POST
//        	//Costruzione jsonString per saveKey
//    		String jsonInputString = "{\"provUsername\": \""+ provUsername +"\"}";
//        	sendPOSTRequest( API_ENDPOINT + "commKeys",jsonInputString);
//        	commKeyProvider = "o richiami getcommkey o la fai riotrnare dalla POST";
//    	}
//	}
//	
//	public void loadSimmetricCipher() {
//		if(commKeyProvider!=null) {
//			simmCipher = new SimmetricCryptography(commKeyProvider);
//			
//		}else {
//			System.out.println("Ricerca la chiave di comunicazione prima di precedere " );
//		}
//	}
//	
//	public void createBreed(String name, String country, String size, String lifeExpectance,
//			String characteristics, String imageURL, String fciGroup) {
//		if(simmCipher!=null) {
//			/*Cifro la breed coN la simmetricKey per evitare il testo in chiaro */
//			String encryptedName = simmCipher.getEncriptedText(name);
//			String encryptedCountry = simmCipher.getEncriptedText(country);
//			String encryptedSize = simmCipher.getEncriptedText(size);
//			String encryptedLifeExpectance = simmCipher.getEncriptedText(lifeExpectance);
//			String encryptedCharacteristics = simmCipher.getEncriptedText(characteristics);
//			String encryptedImageURL = simmCipher.getEncriptedText(imageURL);
//			String encryptedFciGroup = simmCipher.getEncriptedText(fciGroup);
//			
//			// Create json for breed crifrato
//	    	String jsonInputStringForCreateBreed =
//	    			"{\"name\": \""+ encryptedName +"\","
//	    			+ " \"country\": \""+encryptedCountry+"\","
//	    			+ " \"size\": \""+encryptedSize+"\","
//	    			+ " \"lifeExpectance\": \""+encryptedLifeExpectance+"\","
//	    			+ " \"characteristics\": \""+encryptedCharacteristics+"\","
//	    			+ " \"imageURL\": \""+encryptedImageURL+"\","
//	    			+ " \"fciGroup\": \""+encryptedFciGroup+"\""	
//	    			+ "}";
//			
//	    	sendPOSTRequest( API_ENDPOINT + "breeds?provUsername=" + provUsername ,jsonInputStringForCreateBreed);
//		}else {
//			System.out.println("Carica il cifratore simmetrico prima di precedere " );
//		}
//		
//		
//	}
//	
//	
//	
//
//	public void getBreedByName(String breedNameTarget) {
//		if(simmCipher!=null) {
//			BreedDto breedByName = sendGETRequest( API_ENDPOINT + "breeds/name/" + breedNameTarget + "?provUsername=" +  provUsername ,
//					BreedDto.class);
//			if (breedByName != null) {
//	    		System.out.println("BREED: " + breedByName);    
//	    		
//	    		
//	        	String breedDecryptedName = simmCipher.getDecryptedText(breedByName.getName()); // Decrypt value with PrivKey
//	        	String breedDecryptedSize = simmCipher.getDecryptedText(breedByName.getSize());
//	        	String breedDecryptedCountry = simmCipher.getDecryptedText(breedByName.getCountry());
//	        	String breedDecryptedFciGroup = simmCipher.getDecryptedText(breedByName.getFciGroup());
//	        	String breedDecryptedCharacteristics = simmCipher.getDecryptedText(breedByName.getCharacteristics());
//	        	String breedDecryptedImageURL = simmCipher.getDecryptedText(breedByName.getImageURL());
//	        	String breedDecryptedLifeExpectance = simmCipher.getDecryptedText(breedByName.getLifeExpectance());
//	        	
//	        	BreedDto breedFound = new BreedDto();
//	        	
//	        	
//	        	breedFound.setName(breedDecryptedName);
//	        	breedFound.setSize(breedDecryptedSize);
//	        	breedFound.setCharacteristics(breedDecryptedCharacteristics);
//	        	breedFound.setFciGroup(breedDecryptedFciGroup);
//	        	breedFound.setImageURL(breedDecryptedImageURL);
//	        	breedFound.setLifeExpectance(breedDecryptedLifeExpectance);
//	        	breedFound.setCountry(breedDecryptedCountry);
//	        	
//	        	System.out.println("ECCOOLO: " + breedFound);
//	      
//	    		
//	    	}else {
//	    		System.out.println("BREED NOT FOUND "); 
//	    	}
//			
//		}else {
//			System.out.println("Carica il cifratore simmetrico prima di precedere " );
//		}
//		
//		
//	}
//	
//
//	
//	
//	/* GENERIC HTTP METHODS */
//	
//	public Object sendPOSTRequest(String path, String jsonInputString) { 
//    	URL url;
//		try {
//			url = new URL(path);
//			URLConnection con = url.openConnection();
//	    	HttpURLConnection http = (HttpURLConnection)con;
//	    	http.setRequestMethod("POST");
//	    	http.setRequestProperty("Content-Type", "application/json; utf-8");
//	    	http.setRequestProperty("Accept", "application/json");
//	    	http.setDoOutput(true);
//	    	
//	    	
//	    	try(OutputStream os = http.getOutputStream()) {
//	    	    byte[] input = jsonInputString.getBytes("utf-8");
//	    	    os.write(input, 0, input.length);			
//	    	}
//	    	int code = http.getResponseCode();
//			System.out.println(code);
//	    	try(BufferedReader br = new BufferedReader(
//	    			  new InputStreamReader(http.getInputStream(), "utf-8"))) {
//	    			    StringBuilder response = new StringBuilder();
//	    			    String responseLine = null;
//	    			    while ((responseLine = br.readLine()) != null) {
//	    			        response.append(responseLine.trim());
//	    			    }
//	    			    System.out.println(response.toString());
//	    			}
//	    	
//    		return null;
//
//		} catch( Exception e ) {
//    		return null;
//    		}
//    	
//    }
//	
//	public  <T extends Object> T sendGETRequest(String path, Class<T> c) {
//	      
//    	try {
//    		URL url = new URL(path);
//	    	URLConnection con = url.openConnection();
//	    	HttpURLConnection http = (HttpURLConnection)con;
//	    	
//	    	http.setRequestProperty("RSAkey", publicKeyFile.getAbsolutePath());
//	    	http.setRequestMethod("GET"); 
//	    	http.setDoOutput(true);
//
//	    	int responseCode = http.getResponseCode();
//			System.out.println("GET Response Code :: " + responseCode);
//			
//			if (responseCode == HttpURLConnection.HTTP_OK) { // success
//				BufferedReader in = new BufferedReader(new InputStreamReader(
//						con.getInputStream()));
//				String inputLine;
//				StringBuilder response = new StringBuilder();
//	
//				while ((inputLine = in.readLine()) != null) {
//					response.append(inputLine+ '\n');
//				}
//				in.close();
//				
//				
//				return this.g.fromJson(response.toString(), c);
//				
//			} else throw new Exception();
//			
//    	} catch( Exception e ) {
//    		return null;
//    		}
//    }
//
//	
//	public Object sendPUTRequest(String path, String jsonInputString) { 
//    	URL url;
//		try {
//			url = new URL(path);
//			URLConnection con = url.openConnection();
//	    	HttpURLConnection http = (HttpURLConnection)con;
//	    	http.setRequestMethod("PUT");
//	    	http.setRequestProperty("Content-Type", "application/json; utf-8");
//	    	http.setRequestProperty("Accept", "application/json");
//	    	http.setDoOutput(true);
//	    	
//	    	
//	    	try(OutputStream os = http.getOutputStream()) {
//	    	    byte[] input = jsonInputString.getBytes("utf-8");
//	    	    os.write(input, 0, input.length);			
//	    	}
//	    	int code = http.getResponseCode();
//			System.out.println(code);
//	    	try(BufferedReader br = new BufferedReader(
//	    			  new InputStreamReader(http.getInputStream(), "utf-8"))) {
//	    			    StringBuilder response = new StringBuilder();
//	    			    String responseLine = null;
//	    			    while ((responseLine = br.readLine()) != null) {
//	    			        response.append(responseLine.trim());
//	    			    }
//	    			    System.out.println(response.toString());
//	    			}
//	    	
//    		return null;
//
//		} catch( Exception e ) {
//    		return null;
//    		}
//    	
//    }
//}
