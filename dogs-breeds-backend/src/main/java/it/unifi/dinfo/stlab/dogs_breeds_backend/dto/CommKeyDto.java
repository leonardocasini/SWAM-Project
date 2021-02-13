package it.unifi.dinfo.stlab.dogs_breeds_backend.dto;

import it.unifi.dinfo.stlab.dogs_breeds_backend.models.CommKey;

public class CommKeyDto {
	
	
	private String provUsername;
	private String value;
	
	public CommKeyDto() {}
	
	public CommKeyDto(CommKey entity) {
		if(entity == null) return;
		this.value = entity.getValue();
	}



	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getProvUsername() {
		return provUsername;
	}

	public void setProvUsername(String provUsername) {
		this.provUsername = provUsername;
	}

}
