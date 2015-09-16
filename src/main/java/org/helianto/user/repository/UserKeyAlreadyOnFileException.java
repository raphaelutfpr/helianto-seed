package org.helianto.user.repository;


/**
 * Sign-up failed because the provided user key is already on file.
 * 
 * @author mauriciofernandesdecastro
 */
@SuppressWarnings("serial")
public class UserKeyAlreadyOnFileException extends RemoteUserException {
	
	private String userKey;
	
	public UserKeyAlreadyOnFileException(String email) {
		super("userKey already on file");
		this.userKey = email;
	}
	
	public String getUserKey() {
		return userKey;
	}
}
