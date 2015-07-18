package org.helianto.user.repository;

import java.io.Serializable;
import java.util.List;

import org.helianto.core.internal.SimpleCounter;
import org.helianto.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 * User stats repository.
 * 
 * @author mauriciofernandesdecastro
 */
public interface UserStatsRepository 
	extends JpaRepository<User, Serializable> 
{

	/**
	 * Count active users.
	 * 
	 * @param entityId
	 */
	@Query("select new " +
			"org.helianto.core.internal.SimpleCounter" +
			"(parents.parent.userType, count(user)) " +
			"from User user " +
			"join user.parentAssociations parents " +
			"where user.entity.id = ?1 " +
			"and user.userState = 'A' " +
			"group by parents.parent.userType ")
	List<SimpleCounter> countActiveUsersGroupByType(int entityId);

}
