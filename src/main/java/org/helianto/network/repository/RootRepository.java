package org.helianto.network.repository;

import org.helianto.core.domain.Entity;
import org.helianto.security.internal.UserAdapter;
import org.helianto.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Entity network repository.
 * 
 * @author mauriciofernandesdecastro
 */
public interface RootRepository
	extends JpaRepository<User, Integer>
{

	public final static String QUERY_FOR_IDENTITY = "select new "
			+ "org.helianto.security.internal.UserAdapter"
			+ "(child.id, child.entity) "
			+ "from User child "
			+ "join child.parentAssociations parents "
			+ "where lower(parents.parent.userKey) = 'user' "
			+ "and child.identity.id = ?1 ";

	/**
	 * Find ordered user entities by identity id and entity type.
	 * 
	 * @param identityId
	 * @param entityType
	 * @param acitivtyState
	 * @param pageable
	 */
	@Query(value=QUERY_FOR_IDENTITY + "and parents.parent.entity.entityType = ?2 "
			+ "and child.entity.activityState = ?3 "
			+ "and child.userState = ?4 "
			+ "order by child.lastEvent DESC ")
	Page<UserAdapter> findByIdentityIdAndEntityTypeOrderByLastEventDesc(int identityId, char entityType
			, char acitivtyState, char userState, Pageable pageable);
	
	@Query(QUERY_FOR_IDENTITY + "and parents.parent.entity.alias LIKE ?2 "
			+ "order by child.lastEvent DESC ")
	Page<UserAdapter> findByIdentityIdAndEntityOrderByLastEventDesc(int identityId, String search, Pageable pageable);
	
	/**
	 * Find entity by user id (as it is lazy)
	 * 
	 * @param userId
	 */
	@Query("select user.entity "
			+ "from User user "
			+ "where user.id = ?1 ")
	Entity findByUserId(int userId);
	
}
