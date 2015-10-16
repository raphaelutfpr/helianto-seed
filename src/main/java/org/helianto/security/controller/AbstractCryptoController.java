package org.helianto.security.controller;

import javax.inject.Inject;

import org.helianto.core.domain.Identity;
import org.helianto.security.domain.IdentitySecret;
import org.helianto.security.service.IdentityCryptoService;

/**
 * Base class to encrypt tokens.
 * 
 * @author mauriciofernandesdecastro
 */
public class AbstractCryptoController {

	@Inject
	private IdentityCryptoService identityCrypto;
	
	/**
	 * Decrypt and validate token.
	 * 
	 * @param token
	 */
	protected int decriptAndValidateToken(String token) {
		return identityCrypto.decriptAndValidateToken(token);
	}
	
	/**
	 * Encrypt  token.
	 * 
	 * @param identity
	 */
	protected String encryptToken(Identity identity) {
		return identityCrypto.encryptToken(identity);
	}
	
	/**
	 * Identity secret.
	 * 
	 * @param identity
	 */
	protected IdentitySecret getIdentitySecret(Identity identity) {
		return identityCrypto.getIdentitySecretByPrincipal(identity.getPrincipal());
	}
	
	/**
	 * Decript and validate token.
	 * 
	 * @param identity
	 * @param password
	 */
	protected IdentitySecret createIdentitySecret(Identity identity, String password) {
		return identityCrypto.createIdentitySecret(identity, password, false);
	}
	
	/**
	 * Muda a senha de uma identidade.
	 * 
	 * @param principal
	 * @param password
	 */
	public IdentitySecret changeIdentitySecret(String principal, String password){
		return identityCrypto.changeIdentitySecret(principal, password);
	}

}
