package org.helianto.core.social;

import javax.inject.Inject;

import org.helianto.security.internal.UserDetailsAdapter;
import org.helianto.user.domain.User;
import org.helianto.user.repository.UserRepository;

/**
 * Required to sign in users after social providers.
 *  
 * @author mauriciofernandesdecastro
 */
public class UserSocialSignInService {
	
	@Inject
	private UserRepository userRepository;
	
	public void signin(Integer userId){
		User user = userRepository.findOne(userId);
		UserDetailsAdapter userDetails = new UserDetailsAdapter(user);
		SignInUtils.signin(userDetails);
	}

}
