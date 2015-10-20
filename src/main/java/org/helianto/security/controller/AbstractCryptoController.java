package org.helianto.security.controller;

import java.util.List;

import javax.inject.Inject;

import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Identity;
import org.helianto.core.domain.Operator;
import org.helianto.install.service.AbstractEntityInstallStrategy;
import org.helianto.security.domain.IdentitySecret;
import org.helianto.security.service.IdentityCryptoService;
import org.helianto.user.domain.User;

/**
 * Base class to encrypt tokens.
 * 
 * @author mauriciofernandesdecastro
 */
public class AbstractCryptoController extends AbstractEntityInstallStrategy{

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

	@Override
	public List<Entity> generateEntityPrototypes(Object... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDefaultCountry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDefaultStateFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getInitialSecret() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void runOnce(Operator arg0, Entity arg1, User arg2) {
		// TODO Auto-generated method stub
		
	}
	

}
