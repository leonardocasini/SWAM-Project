package it.unifi.dinfo.stlab.dogs_breeds_backend;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import assimetric.AsymmetricCryptography;
import assimetric.GeneratorKeys;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dto.BreedDto;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dto.CommKeyDto;
import simmetric.SimmetricCryptography;

import com.google.gson.Gson;


/*Now it's all done by client's project*/
//public class MyApplication {
//
//	private final static String PUBLIC_KEY = "primoprovider/public.key";
//	private final static String PRIVATE_KEY = "primoprovider/private.key";
//	
//	// Dichiarati come attributi ma sarebbe meglio inserirli
//	
//	static String provUsername = "primoprovider";
//	private final static String PASSWORD = "password must be strong"; // non si passa più ma viene auto-generata
//	
//	private static File publicKeyFile;
//	private static File privateKeyFile;
//	private static String commKeyProvider;
//
//	static Gson g = new Gson();
//	// Our ENDPOINT
//	private final static String API_ENDPOINT = "http://localhost:8080/dogs-breeds-backend/api/";
//	
//    public static void main(String[] args) throws Exception {
//
//    	
//    	/* 1. Try to read my public & private keys,  if no keys found, generate and save */
//    	
//    	publicKeyFile = new File(PUBLIC_KEY);
//    	privateKeyFile = new File(PRIVATE_KEY);
//    	
//    	if( !publicKeyFile.exists()  || !privateKeyFile.exists()) { // Check if files exist or not
//    		
//    		//If not exists
//        	GeneratorKeys generator = new GeneratorKeys(512); 
//        	/* This method generate the keys and save them in those paths*/
//        	generator.saveKeys(PUBLIC_KEY, PRIVATE_KEY); 
//        	
//        	System.out.println("PUB & PRIV KEYS GENERATED");
//        	
//        	publicKeyFile = new File(PUBLIC_KEY);
//        	privateKeyFile = new File(PRIVATE_KEY);
//        	
//    	} else {
//    		
//    		System.out.println("KEYS FOUND");
//    		
//    	}
//
//    	System.out.println("KEY PATH : " + publicKeyFile.getAbsolutePath());
//    	
//    	/* 2. Generate Asymmetric keys */
//    	
//    	AsymmetricCryptography ac = new AsymmetricCryptography();
//    	
//    	ac.loadPublicKey(publicKeyFile.getCanonicalPath()); //  Load public key by path 
//    	ac.loadPrivateKey(privateKeyFile.getCanonicalPath()); // Load private key by path 
//
//
////    	String cazzo = "MAyeVl68mJRodi9BahziZA6XkxXI+Hqq7Y1/sEl9opOI3UZTmWzHsVdZyX1D4lz2YJV8a+egYXjMFR+1N9eF/Q==";
////    	
////    	String cazzoPub = ac.encryptText(cazzo);
////    	
////    	System.out.println(ac.decryptText(cazzoPub));
//    	
//    	
////    	String name = encrypt("Bassotto");
////    	String country = encrypt("Germania");
////    	String size = encrypt("piccolo");
////    	String lifeExpectance = encrypt("12y");
////    	String characteristics = encrypt("docile");
////    	String imageURL = encrypt("google");
////    	String fciGroup = encrypt("b");
////    	
////    	System.out.println("Name:" +  " " + name +   " " + "country:" + " " + country + " " +  "size:"  +  " "  + size  +
////    			"lifeExpectance:" +  " " + lifeExpectance +   " " + "characteristics:" + " " + characteristics + " " +  "imageURL:"  +  " "  + imageURL +
////    			"fciGroup:" +  " " + fciGroup);
//    	
//    	
//    	/* 3. Get commKey by provid*/
//
//    	// Get that returns CommKey Value by provUsername
//    	CommKeyDto commKey = sendGETRequest( API_ENDPOINT + "commKeys/provUsername/" + provUsername, CommKeyDto.class);
//    	    	
//    	if (commKey != null) {
//    		System.out.println("Provider key : " + commKey);    		
//            	
//        	commKeyProvider = ac.decryptText(commKey.getValue()); //Decrypt value with PrivKey
//        	
//        	
//        	System.out.println("Provider decripted key: " + commKeyProvider );
//    		
//    	}else {
//    		//If not exists create into Server POST
//        	//Costruzione jsonString per saveKey
//    		String jsonInputString = "{\"provUsername\": \""+ provUsername +"\", \"value\": \""+PASSWORD+"\"}";
//        	sendPOSTRequest( API_ENDPOINT + "commKeys",jsonInputString);
//        	commKeyProvider = "o richiami getcommkey o la fai riotrnare dalla POST";
//    	}
//  
//      	/*4. Una volta che hai la commkey posso effettuare i metodi CRUD*/
//    	
//
//    	
//    	
//    	/* CREAZIONE RAZZA  */
//    	SimmetricCryptography sc = new SimmetricCryptography(commKeyProvider);
//    	/*Cifro la breed coN la simmetricKey per evitare il testo in chiaro */
//    	String encryptedName = sc.getEncriptedText("German Sheppard");
//    	String encryptedCountry = sc.getEncriptedText("Germany");
//    	String encryptedSize = sc.getEncriptedText("Big");
//    	String encryptedLifeExpectance = sc.getEncriptedText("15Y");
//    	String encryptedCharacteristics = sc.getEncriptedText("Cute");
//    	String encryptedImageURL = sc.getEncriptedText("Yahoo");
//    	String encryptedFciGroup = sc.getEncriptedText("C");
//    	
//    	
//    	// Create json for breed crifrato
//    	String jsonInputStringForCreateBreed =
//    			"{\"name\": \""+ encryptedName +"\","
//    			+ " \"country\": \""+encryptedCountry+"\","
//    			+ " \"size\": \""+encryptedSize+"\","
//    			+ " \"lifeExpectance\": \""+encryptedLifeExpectance+"\","
//    			+ " \"characteristics\": \""+encryptedCharacteristics+"\","
//    			+ " \"imageURL\": \""+encryptedImageURL+"\","
//    			+ " \"fciGroup\": \""+encryptedFciGroup+"\""	
//    			+ "}";
//    	
//    	//sendPOSTRequest( API_ENDPOINT + "breeds?provUsername=" + provUsername ,jsonInputStringForCreateBreed);
//    	
//    	
////    	/* GET di una razza by name */
//    	String breedTarget = "German Sheppard";
//    	BreedDto breedByName = sendGETRequest( API_ENDPOINT + "breeds/name/" + breedTarget + "?provUsername=" +  provUsername , BreedDto.class);
//    	
//    
//    	
//    	if (breedByName != null) {
//    		System.out.println("BREED: " + breedByName);    
//    		
//    		
//        	String breedDecryptedName = sc.getDecryptedText(breedByName.getName()); // Decrypt value with PrivKey
//        	String breedDecryptedSize = sc.getDecryptedText(breedByName.getSize());
//        	String breedDecryptedCountry = sc.getDecryptedText(breedByName.getCountry());
//        	String breedDecryptedFciGroup = sc.getDecryptedText(breedByName.getFciGroup());
//        	String breedDecryptedCharacteristics = sc.getDecryptedText(breedByName.getCharacteristics());
//        	String breedDecryptedImageURL = sc.getDecryptedText(breedByName.getImageURL());
//        	String breedDecryptedLifeExpectance = sc.getDecryptedText(breedByName.getLifeExpectance());
//        	
//        	BreedDto breedFound = new BreedDto();
//        	
//        	
//        	breedFound.setName(breedDecryptedName);
//        	breedFound.setSize(breedDecryptedSize);
//        	breedFound.setCharacteristics(breedDecryptedCharacteristics);
//        	breedFound.setFciGroup(breedDecryptedFciGroup);
//        	breedFound.setImageURL(breedDecryptedImageURL);
//        	breedFound.setLifeExpectance(breedDecryptedLifeExpectance);
//        	breedFound.setCountry(breedDecryptedCountry);
//        	
//        	System.out.println("ECCOOLO: " + breedFound);
//      
//    		
//    	}else {
//    		
//    	}
//    	
//    	
//    	
//    	
//    }
//
//	    private static Object sendPOSTRequest(String path, String jsonInputString) { //Map<String, Object> params
//	    	URL url;
//			try {
//				url = new URL(path);
//				URLConnection con = url.openConnection();
//		    	HttpURLConnection http = (HttpURLConnection)con;
//		    	http.setRequestMethod("POST");
//		    	http.setRequestProperty("Content-Type", "application/json; utf-8");
//		    	http.setRequestProperty("Accept", "application/json");
//		    	http.setDoOutput(true);
//		    	
//		    	
//		    	try(OutputStream os = http.getOutputStream()) {
//		    	    byte[] input = jsonInputString.getBytes("utf-8");
//		    	    os.write(input, 0, input.length);			
//		    	}
//		    	int code = http.getResponseCode();
//				System.out.println(code);
//		    	try(BufferedReader br = new BufferedReader(
//		    			  new InputStreamReader(http.getInputStream(), "utf-8"))) {
//		    			    StringBuilder response = new StringBuilder();
//		    			    String responseLine = null;
//		    			    while ((responseLine = br.readLine()) != null) {
//		    			        response.append(responseLine.trim());
//		    			    }
//		    			    System.out.println(response.toString());
//		    			}
//		    	
//	    		return null;
//
//			} catch( Exception e ) {
//	    		return null;
//	    		}
//	    	
//	    }
//    
//        private static <T extends Object> T sendGETRequest(String path, Class<T> c) {
//      
//	    	try {
//	    		URL url = new URL(path);
//		    	URLConnection con = url.openConnection();
//		    	HttpURLConnection http = (HttpURLConnection)con;
//		    	
//		    	http.setRequestProperty("RSAkey", publicKeyFile.getAbsolutePath());
//		    	http.setRequestMethod("GET"); 
//		    	http.setDoOutput(true);
//	
//		    	int responseCode = http.getResponseCode();
//				System.out.println("GET Response Code :: " + responseCode);
//				
//				if (responseCode == HttpURLConnection.HTTP_OK) { // success
//					BufferedReader in = new BufferedReader(new InputStreamReader(
//							con.getInputStream()));
//					String inputLine;
//					StringBuilder response = new StringBuilder();
//		
//					while ((inputLine = in.readLine()) != null) {
//						response.append(inputLine+ '\n');
//					}
//					in.close();
//					
//					//return response.toString();
//					return g.fromJson(response.toString(), c);
//					
//				} else throw new Exception();
//				
//	    	} catch( Exception e ) {
//	    		return null;
//	    		}
//	    }
//        
//    private static String encrypt(String string) {
//    	
//    	SimmetricCryptography sc = new SimmetricCryptography("leoilbello");
//    	
//    	String encryptedString = sc.getEncriptedText(string);
//    	
//    	return encryptedString;
//    	
//    }
//    
//    
//}
//
//    
