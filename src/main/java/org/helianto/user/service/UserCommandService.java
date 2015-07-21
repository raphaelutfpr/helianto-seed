package org.helianto.user.service;

import java.util.Locale;

import javax.inject.Inject;

import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Identity;
import org.helianto.core.repository.EntityRepository;
import org.helianto.core.repository.IdentityRepository;
import org.helianto.user.domain.User;
import org.helianto.user.repository.UserReadAdapter;
import org.helianto.user.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
* User command service.
* 
* @author Eldevan Nery Junior
*/
@Service
public class UserCommandService {

	@Inject 
	private IdentityRepository identityRepository;
	
	@Inject
	private EntityRepository entityRepository;
	
	@Inject 
	private UserRepository userRepository;
	
	public User user(int entityId, UserReadAdapter command) {
		User target = null;
		if (command.getUserId()==0) {
			target = newUserAdapter(command.getIdentityId(), entityId).build().merge();
			Integer existing = userRepository.findIdByEntity_IdAndUserKey(entityId, target.getUserKey());
			if (existing!=null) {
				throw new RuntimeException("USER_NOT_UNIQUE");
			}
		}
		else{
			target = userRepository.findOne(command.getUserId());
		}
		target = userRepository.saveAndFlush(command.setAdaptee(target).build().merge());
		
		return userRepository.findOne(target.getId());
	}
	
	public UserReadAdapter newUserAdapter(Integer identityId, Integer entityId){
		return new UserReadAdapter(createUser(identityId, entityId));
	}
	
	/**
	 * Cria um Usuário.
	 * 
	 * @param identity
	 * @param entity
	 */
	public User createUser(Integer identityId, Integer entityId) {
		Identity identity = identityRepository.findOne(identityId);
		Entity entity = entityRepository.findOne(entityId);
		User user = null;
		if (identity!=null) {
			user = new User(entity, identity);
			user.setUserName(identity.getIdentityName());
			user.setLocale(Locale.getDefault());
			user.setUserType('I');
			user.setUserState('A');
			user.setAccountNonExpired(true);
			return user;
		}
		throw new IllegalArgumentException("Identity not informed!");
	}
	
}
