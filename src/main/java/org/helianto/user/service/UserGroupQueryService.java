package org.helianto.user.service;

import javax.inject.Inject;

import org.helianto.user.domain.UserGroup;
import org.helianto.user.repository.UserGroupRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
* User group query service.
* 
* @author mauriciofernandesdecastro
*/
@Service
public class UserGroupQueryService {

	@Inject
	private UserGroupRepository userGroupRepository;

	/**
	 * List user groups by entity.
	 * 
	 * @param entityId
	 * @param userType
	 * @param pageNumber
	 * @param pageSize
	 */
	public Page<UserGroup> listUserGroup(int entityId, char userType, Integer pageNumber, Integer pageSize) {
		Pageable page = new PageRequest(pageNumber, pageSize, Direction.ASC, "userName");
		Page<UserGroup> userGroupReaderAdapterList =
				userGroupRepository.findActiveUserGroupsByUserType(entityId, userType, page);
		return userGroupReaderAdapterList;
	}
	
	/**
	 * List user groups by user.
	 * 
	 * @param model
	 * @param userId
	 * @param userGroupListPage
	 */
	public Page<UserGroup> listUserGroup(int userId, Integer pageNumber, Integer pageSize) {
		Pageable page = new PageRequest(pageNumber, pageSize, Direction.ASC, "parent.userKey");
		Page<UserGroup> userGroupAdapterList =
				userGroupRepository.find2ParentsByChildId(userId, page);
		return userGroupAdapterList;
	}
	
}
