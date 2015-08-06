package org.helianto.home.repository;

import java.io.Serializable;
import java.util.List;

import org.helianto.core.domain.Category;
import org.helianto.core.internal.SimpleCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 * Category stats repo.
 * 
 * @author mauriciofernandesdecastro
 */
public interface CategoryStatsRepository 
	extends JpaRepository<Category, Serializable> {
		
	/**
	 * Count by entity.
	 * 
	 * @param entityId
	 */
	@Query("select new " +
			"org.helianto.core.internal.SimpleCounter" +
			"(category.categoryGroup, count(category)) " +
			"from Category category " +
			"where category.entity.id = ?1 " +
			"group by category.categoryGroup")
	List<SimpleCounter> countCategoriesByEntityIdGroupByGroup(int entityId);


}
