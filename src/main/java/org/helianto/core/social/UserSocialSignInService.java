package org.helianto.core.social;

import javax.inject.Inject;

import org.helianto.core.social.SignInUtils;
import org.helianto.security.internal.UserDetailsAdapter;
import org.helianto.user.domain.User;
import org.helianto.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserSocialSignInService {
	
	@Inject
	private UserRepository userRepository;
	
	public void signin(Integer userId){
		User user = userRepository.findOne(userId);
		UserDetailsAdapter userDetails = new UserDetailsAdapter(user);
		SignInUtils.signin(userDetails);
	}

}
