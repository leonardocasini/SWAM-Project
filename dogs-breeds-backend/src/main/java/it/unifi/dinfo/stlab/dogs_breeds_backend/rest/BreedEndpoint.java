package it.unifi.dinfo.stlab.dogs_breeds_backend.rest;


import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import it.unifi.dinfo.stlab.dogs_breeds_backend.controllers.BreedController;
import it.unifi.dinfo.stlab.dogs_breeds_backend.controllers.CommKeyController;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dto.BreedDto;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dto.BreedingFarmDto;
import it.unifi.dinfo.stlab.dogs_breeds_backend.dto.CommKeyDto;
import simmetric.SimmetricCryptography;

@Path("breeds")
public class BreedEndpoint {
	
	
	@Inject BreedController breedController;
	@Inject CommKeyController commKeyController;
		
		
	/* Create a new Breed */
	@POST
	@Consumes({MediaType.APPLICATION_JSON})  
    @Produces({MediaType.APPLICATION_JSON})
	public Response createBreed( 	
			@QueryParam("provUsername") String provUsername, 
			BreedDto breedDto) throws GeneralSecurityException{
						
		/*Creating Breed*/		
		if (breedController.createBreed(breedDto, provUsername) != null){		
			return Response.status(Response.Status.CREATED).build();	 		
		}else {
			return Response.status(Response.Status.BAD_REQUEST).build();	
		}
		
	}
	
	/* GET all Breeds */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getAll(
			@QueryParam("provUsername") String provUsername) {
		
		
		List<BreedDto> encryptedDtos = breedController.getAllByProvUsername(provUsername);		
        return Response.status(Response.Status.OK).entity(encryptedDtos).build();				

	}
	
	/* Update an existing Breed */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateBreed( 
			BreedDto breedDto, 
			@QueryParam("provUsername") String provUsername
			) {
		
		breedController.updateBreed(breedDto, provUsername);				
		
		return Response.status(Response.Status.OK).build();	   
		
	}
		
	/* Delete a Breed */
	@DELETE
	@Path("/{breedId}")
	public Response deleteBreed( 
			@PathParam("breedId") Long breedId ) {
				
		if(!breedController.deleteBreed(breedId)) {
			// Code 204 - No Content
			return Response.noContent().build();		
		} else {
			return Response.status(Response.Status.OK).build();
		
		}
	}
		
	/* Get Breed by name */
	@GET
	@Path("/name/{name}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getByName(
    		@QueryParam("provUsername") String provUsername,
    		@PathParam("name") String name) {
		
		
		BreedDto encryptedDtoByCommKeyProvider = breedController.getByName(name, provUsername);	
        return Response.status(Response.Status.OK).entity(encryptedDtoByCommKeyProvider).build();
        
    }
		
	/* Get Breeds by country */
	@GET
	@Path("/country/{country}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getByCountry(
    		@QueryParam("provUsername") String provUsername,
    		@PathParam("country") String country) {
	    
		
		List<BreedDto> encryptedDtos = breedController.getByCountry(country, provUsername);
        return Response.status(Response.Status.OK).entity(encryptedDtos).build();
        
    }
	
	/* Get Breed by Id */
	@GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(
    		@QueryParam("provUsername") String provUsername,
    		@PathParam("id") Long id) {
        		
     		
		BreedDto encryptedDtoByCommKeyProvider = breedController.getById(id, provUsername);
        return Response.status(Response.Status.OK).entity(encryptedDtoByCommKeyProvider).build();        
        
    }
	
	/* Get Breeds by Size */
	@GET
    @Path("size/{size}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getBySize(
    		@QueryParam("provUsername") String provUsername,
    		@PathParam("size") String size) {
		
		List<BreedDto> encryptedDtos = breedController.getByCountry(size, provUsername);
        return Response.status(Response.Status.OK).entity(encryptedDtos).build();
		
    }
	
	/* Get Breeds by FciGroup */
	@GET
    @Path("fciGroup/{fciGroup}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getByFciGroup(
    		@QueryParam("provUsername") String provUsername,
    		@PathParam("fciGroup") String fciGroup) {
		
		List<BreedDto> encryptedDtos = breedController.getByCountry(fciGroup, provUsername);
        return Response.status(Response.Status.OK).entity(encryptedDtos).build();
    
	}
	
	/* Get FARMS for Breed*/
	@GET
	@Path("/idBreed/{idBreed}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getFarmsOfBreed(
    		@QueryParam("provUsername") String provUsername,
    		@PathParam("idBreed") Long idBreed) {
			
		Set<BreedingFarmDto> farms = breedController.getBreedingFarmOfBreed(idBreed, provUsername);
		List<BreedingFarmDto> list = new ArrayList<>(farms);
        return Response.status(Response.Status.OK).entity(list).build();
        
    }
	
	
	

	
	
	
	
	
	
	
}

