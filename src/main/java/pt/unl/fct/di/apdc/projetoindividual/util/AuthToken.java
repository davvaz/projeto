package pt.unl.fct.di.apdc.projetoindividual.util;

import java.util.UUID;

public class AuthToken {
	
	public static final long EXPIRATION_TIME = 1000*60*20; //2h 1000*60*60*2  //// 1000*60*20
	
	public String username;
	

	public String tokenID;
	public String role;
	public long creationData;
	public long expirationData;
	
	public AuthToken() {
		
	}
	
	public AuthToken(String username, String role) {
		this.username = username;
		this.role = role;
		this.tokenID = UUID.randomUUID().toString();
		this.creationData = System.currentTimeMillis();
		this.expirationData = this.creationData + AuthToken.EXPIRATION_TIME;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTokenID() {
		return tokenID;
	}

	public void setTokenID(String tokenID) {
		this.tokenID = tokenID;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public long getCreationData() {
		return creationData;
	}

	public void setCreationData(long creationData) {
		this.creationData = creationData;
	}

	public long getExpirationData() {
		return expirationData;
	}

	public void setExpirationData(long expirationData) {
		this.expirationData = expirationData;
	}

	public static long getExpirationTime() {
		return EXPIRATION_TIME;
	}

}
