package org.helianto.user.service;

import java.util.List;

import javax.inject.Inject;

import org.helianto.core.internal.KeyNameAdapter;
import org.helianto.core.internal.QualifierAdapter;
import org.helianto.core.internal.SimpleCounter;
import org.helianto.user.domain.User;
import org.helianto.user.repository.UserRepository;
import org.helianto.user.repository.UserStatsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

/**
* User query service.
* 
* @author mauriciofernandesdecastro
*/
public class UserQueryService {

	@Inject 
	private UserRepository userRepository;

	@Inject 
	protected UserStatsRepository userStatsRepository;
	
	private KeyNameAdapter[] keyNameAdapter;
	
	/**
	 * KeyNameAdapter array constructor.
	 * 
	 * @param keyNameAdapter
	 */
	public UserQueryService(KeyNameAdapter[] keyNameAdapter) {
		super();
		this.keyNameAdapter = keyNameAdapter;
	}

	/**
	 * List qualifiers.
	 * 
	 * @param userAuthentication
	 */
	public List<QualifierAdapter> qualifierList(int entityId) {
		List<QualifierAdapter> qualifierList = 
				QualifierAdapter.qualifierAdapterList(keyNameAdapter);
		qualifierCount(entityId, qualifierList);
		return qualifierList;
	}
	
	/**
	 * Helper method to count qualifiers.
	 * 
	 * @param entityId
	 * @param qualifierList
	 */
	protected void qualifierCount(int entityId, List<QualifierAdapter> qualifierList) {
		List<SimpleCounter> counterListAll 
			= userStatsRepository.countActiveUsersGroupByType(entityId);
		for (QualifierAdapter qualifier: qualifierList) {
			qualifier.setCountItems(counterListAll);
		}
	}
	
	/**
	 * Page users.
	 * 
	 * @param entityId
	 * @param userType
	 * @param userStates
	 * @param pageNumber
	 * @param itemsPerPage
	 */
	public Page<User> userList(int entityId, Character userType, String userStates, Integer pageNumber, Integer itemsPerPage) {
		Pageable page = new PageRequest(pageNumber, itemsPerPage, Direction.ASC, "userName");
		return userRepository.findByParentUserType(entityId, userType, userStates.toCharArray(), page);
	}
	
	/**
	 * Get user.
	 * 
	 * @param userId
	 */
	public User user(int entityId, Integer userId) {
		User user = userRepository.findAdapter(userId);
		return user;
	}

}
