package it.unifi.dinfo.stlab.dogs_breeds_backend.utils;

import java.util.UUID;

public class RandomStringGenerator {
	
	public static String generateString() {
		
        String uuid = UUID.randomUUID().toString();
        return uuid;
        
    }

}
