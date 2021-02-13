package it.unifi.dinfo.stlab.dogs_breeds_backend.mappers;

import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import it.unifi.dinfo.stlab.dogs_breeds_backend.dto.BreedDto;
import it.unifi.dinfo.stlab.dogs_breeds_backend.models.Breed;

@RequestScoped
public class BreedMapper {
	
	@Inject BreedingFarmMapper farmMapper;
	
	
	public BreedMapper() {}
	
	
	/* Entity -> DTO */
	public BreedDto toDto(Breed entity) {
		
		if(entity == null)
            return null;
		
		BreedDto dto = new BreedDto();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setCountry(entity.getCountry());
		dto.setSize(entity.getSize());
		dto.setLifeExpectance(entity.getLifeExpectance());
		dto.setCharacteristics(entity.getCharacteristics());
		dto.setFciGroup(entity.getFciGroup());
		dto.setImageURL(entity.getImageURL());
		dto.setBreedingFarms(entity.getBreedingFarms().stream().map(farmMapper::toDto).collect(Collectors.toSet()));
		
		
		
		return dto;		
	}
	
	/* DTO -> Entity */
	public Breed toEntity (BreedDto dto, Breed b) {
		
		if(dto == null)
            return null;
        if(b == null)
            return null;
        
        b.setId(dto.getId());
		b.setName(dto.getName());
		b.setCountry(dto.getCountry());	
		b.setSize(dto.getSize());
		b.setCharacteristics(dto.getCharacteristics());
		b.setFciGroup(dto.getFciGroup());
		b.setLifeExpectance(dto.getLifeExpectance());
		b.setImageURL(dto.getImageURL());
		b.setBreedingFarms(
				dto.getBreedingFarms().stream().map(farmMapper::toEntity).collect(Collectors.toSet())
			);
		
		return b;
	}
	
	
	
	


	
	
	
	

}
