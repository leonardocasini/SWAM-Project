package it.unifi.dinfo.stlab.dogs_breeds_backend.mappers;

import javax.enterprise.context.RequestScoped;

import it.unifi.dinfo.stlab.dogs_breeds_backend.dto.CommKeyDto;
import it.unifi.dinfo.stlab.dogs_breeds_backend.models.CommKey;

@RequestScoped
public class CommKeyMapper {
	
	public CommKeyMapper() {}
	
	/* Entity -> DTO */
	public CommKeyDto toDto(CommKey entity) {
		
		if(entity == null)
            return null;
		
		CommKeyDto dto = new CommKeyDto();
		dto.setValue(entity.getValue());
		dto.setProvUsername(entity.getProvUsername());	
		return dto;		
	}
	
	/* DTO -> Entity */
	public CommKey toEntity (CommKeyDto dto, CommKey k) {
		
		if(dto == null)
            return null;
        if(k == null)
            return null;
	
		k.setValue(dto.getValue());
		k.setProvUsername(dto.getProvUsername());
		
		return k;
	}
	

}