package it.unifi.dinfo.stlab.dogs_breeds_backend.rest;

import java.security.GeneralSecurityException;
import java.util.List;


import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import assimetric.AsymmetricCryptography;
import it.unifi.dinfo.stlab.dogs_breeds_backend.controllers.CommKeyController;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dto.CommKeyDto;
import simmetric.SimmetricCryptography;

@Path("commKeys")
public class CommKeyEndpoint {
	
	
	@Inject CommKeyController commKeyController;
	
	/* Internal key for our module */
	String internalPassword = "lsgCsgdld";
	SimmetricCryptography c = new SimmetricCryptography(internalPassword);
		
	
	/* Get commKey's value by Provider's username */
	
	@GET
	@Path("/provUsername/{provUsername}")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getCommKeyByProvId( 
		@Context HttpHeaders headers,
		@PathParam("provUsername") String provUsername
		){
			
											
				CommKeyDto dto = commKeyController.getByProvUsername(provUsername);
				String value = dto.getValue(); // Chiave recuperata
				
				/* Decriptaggio della chiave */				
				String decryptedValue = c.getDecryptedText(value);
				
				
				
				String pathToPubKey = headers.getRequestHeader("RSAkey").get(0);
				
				
				/* Carico la chiave pubblica nella classe  */
				AsymmetricCryptography ac = new AsymmetricCryptography();
				ac.loadPublicKey(pathToPubKey);
				
				/* Cifro con la chiave pubblica e la faccio ritornare  */
				String encryptedValueByPubKey = ac.encryptText(decryptedValue);
				
				CommKeyDto result = new CommKeyDto();
				result.setProvUsername(provUsername);
				result.setValue(encryptedValueByPubKey);
	        return Response
	        		.status(Response.Status.OK)
	        		.entity(result)
	        		.build();	
	        
		
					
	}
		
	/* Save our commKey, at this point both Provider and module share the commKey */
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})  
    @Produces({MediaType.APPLICATION_JSON})
	public Response saveCommKey(@Context HttpHeaders headers, CommKeyDto dto) throws GeneralSecurityException{
		
		/* If breed with this name already exists, breedFound = true, return noContent */
		
		boolean commKeyFound = false;
		
		List<CommKeyDto> commKeyDtos = commKeyController.getAll();
		
		
		for(int i = 0; i< commKeyDtos.size(); i++) {
			
			if(commKeyDtos.get(i).getProvUsername().equals(dto.getProvUsername())){
				commKeyFound = true;			
			}		
		}
		
		if(commKeyFound == false) {
			
			commKeyController.saveCommKey(dto);
			
			return Response.status(Response.Status.OK).build();	
			
		} else {
			
			System.out.println("Provider username già presente");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();			
		}
		
	}
	
	/* GET by Id */
	
	@GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") Long id) {
        
		CommKeyDto dto = commKeyController.getById(id);
        return Response.status(Response.Status.OK).entity(dto).build();
    }
	
	
	
	
	

	
	
	

	
	
	
	
	
	

}
