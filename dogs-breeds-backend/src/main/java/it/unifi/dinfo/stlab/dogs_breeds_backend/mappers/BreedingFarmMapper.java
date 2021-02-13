package it.unifi.dinfo.stlab.dogs_breeds_backend.mappers;

import javax.enterprise.context.RequestScoped;

import it.unifi.dinfo.stlab.dogs_breeds_backend.dto.BreedingFarmDto;
import it.unifi.dinfo.stlab.dogs_breeds_backend.models.BreedingFarm;

@RequestScoped
public class BreedingFarmMapper {

	public BreedingFarmMapper() {}
	
	/* Entity -> DTO */
	public BreedingFarmDto toDto(BreedingFarm entity) {
		
		if(entity == null)
            return null;
		
		BreedingFarmDto dto = new BreedingFarmDto();
		dto.setId(entity.getId());
		dto.setFarmName(entity.getFarmName());
		dto.setAddress(entity.getAddress());
		dto.setTelephone(entity.getTelephone());

		
		
		
		return dto;
	
	}
	
	
	/* DTO -> Entity */
	public BreedingFarm toEntity (BreedingFarmDto dto) {
		 BreedingFarm b = new BreedingFarm();
		
		if(dto == null)
            return null;
        if(b == null)
            return null;
        
        b.setId(dto.getId());
        b.setFarmName(dto.getFarmName());
        b.setAddress(dto.getAddress());
        b.setTelephone(dto.getTelephone());
        
     
        return b;
	}
}
