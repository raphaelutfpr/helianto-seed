package org.helianto.security.repository;

import java.io.Serializable;

import org.helianto.core.domain.Signup;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Signup repository.
 * 
 * @author mauriciofernandesdecastro
 */
public interface SignupTmpRepository extends JpaRepository<Signup, Serializable> {

	public Signup findByToken(String toke);
	
}
