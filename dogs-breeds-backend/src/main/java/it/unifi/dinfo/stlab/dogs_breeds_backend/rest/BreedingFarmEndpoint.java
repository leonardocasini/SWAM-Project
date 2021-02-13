package it.unifi.dinfo.stlab.dogs_breeds_backend.rest;

import java.security.GeneralSecurityException;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.unifi.dinfo.stlab.dogs_breeds_backend.controllers.BreedController;
import it.unifi.dinfo.stlab.dogs_breeds_backend.controllers.BreedingFarmController;
import it.unifi.dinfo.stlab.dogs_breeds_backend.controllers.CommKeyController;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dto.BreedDto;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dto.BreedingFarmDto;

@Path("farms")
public class BreedingFarmEndpoint {

	@Inject BreedingFarmController breedingFarmController;
	@Inject BreedController breedController;
	@Inject CommKeyController commKeyController;
	
	/* Add Farm to a Breed */
	@POST
	@Path("/{idFarm}/addBreed")
	@Consumes({MediaType.APPLICATION_JSON})  
    @Produces({MediaType.APPLICATION_JSON})
	public Response addBreed( 	
			@QueryParam("provUsername") String provUsername,
			@PathParam("idFarm") Long idFarm, 
			BreedDto dto
			) throws GeneralSecurityException{
		
				
				breedController.addBreedingFarmToBreed(dto, idFarm, provUsername);
				
				
				return Response.status(Response.Status.OK).build();	 		
				
		
	}
	
	/* Create a new BreedingFarm */
	@POST
	@Consumes({MediaType.APPLICATION_JSON})  
    @Produces({MediaType.APPLICATION_JSON})
	public Response createBreedingFarm( 	
			@QueryParam("provUsername") String provUsername, 
			BreedingFarmDto breedingFarmDto) throws GeneralSecurityException{
						
			/*Creating BreedingFarm*/		
			breedingFarmController.createBreedingFarm(breedingFarmDto, provUsername); 	
			return Response.status(Response.Status.CREATED).build();	 		
		
		
	}
	
	/* Get Breed by name */
	@GET
	@Path("/farmName/{farmName}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getByName(
    		@QueryParam("provUsername") String provUsername,
    		@PathParam("farmName") String farmName) {
			
		BreedingFarmDto encryptedDtoByCommKeyProvider = breedingFarmController.getByName(farmName, provUsername);	
        return Response.status(Response.Status.OK).entity(encryptedDtoByCommKeyProvider).build();
        
    }
	
	
	
	
	
	
	
}
