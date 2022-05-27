package com.example.ems.util;

import java.security.SecureRandom;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class GeneratePassword {
	private SecureRandom random;
	
	public SecureRandom getObject() {
		return new SecureRandom();
	}
	
	public GeneratePassword() {
		random = getObject();
	}
	
	// Method to generate a random alphanumeric password of a specific length
    public String generateRandomPassword(int len, int randNumOrigin, int randNumBound)
    {
        return random.ints(len, randNumOrigin, randNumBound + 1)
                    .mapToObj(i -> String.valueOf((char)i))
                    .collect(Collectors.joining());
    }
}
