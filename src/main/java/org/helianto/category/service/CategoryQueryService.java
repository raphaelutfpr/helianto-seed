package org.helianto.category.service;

import java.util.List;

import javax.inject.Inject;

import org.helianto.core.def.CategoryGroup;
import org.helianto.core.domain.Category;
import org.helianto.core.internal.QualifierAdapter;
import org.helianto.core.internal.SimpleCounter;
import org.helianto.core.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * Category query service.
 * 
 * @author mauriciofernandesdecastro
 */
@Service
public class CategoryQueryService {

	@Inject
	private CategoryRepository categoryRepository;
	
	/**
	 * List category by group.
	 * 
	 * @param entityId
	 */
	public List<QualifierAdapter> qualifier(int entityId) {
		List<QualifierAdapter> qualifierList 
			= QualifierAdapter.qualifierAdapterList(CategoryGroup.values());
	
		// counting
		qualifierCount(entityId, qualifierList);
		
		return qualifierList;
	}
	
	/**
	 * Helper method to count qualifiers.
	 * 
	 * @param entityId
	 * @param qualifierList
	 */
	public void qualifierCount(Integer entityId, List<QualifierAdapter> qualifierList) {
		
		// count grouped categories
		List<SimpleCounter> counterListAll 
			= categoryRepository.countCategoriesByEntityIdGroupByGroup(entityId);
		
		// count individually
		for (QualifierAdapter qualifier: qualifierList) {
			qualifier.setCountItems(counterListAll);
		}
				
	}

	/**
	 * Page first 100 categories.
	 * 
	 * @param entityId
	 * @param categoryGroupType
	 * @param pageNumber
	 */
	public Page<Category> category(int entityId, CategoryGroup categoryGroupType, Integer pageNumber) {
		Pageable page = new PageRequest(pageNumber, 100, Direction.ASC, "categoryCode");
		return categoryRepository.findByEntity_IdAndCategoryGroupType(entityId, categoryGroupType, page);
	}
	
	/**
	 * Search category.
	 * 
	 * @param entityId
	 * @param categoryGroup
	 * @param pageNumber
	 * @param search
	 * @param orderString
	 */
	public Page<Category> categorySearch(int entityId, Character categoryGroup, Integer pageNumber, String search, String orderString) {
		Pageable page = new PageRequest(pageNumber, 20, Direction.ASC, orderString);
		char[] categoryGroups = {categoryGroup} ;
		if(categoryGroup.equals('_')){
			 categoryGroups = CategoryGroup.valuesAsArray();
		}
		return categoryRepository
				.findByEntityIdAndCategoryGroupAndCategoryCodeLikeOrCategoryNameLike(entityId, categoryGroups, search, page);
	}
	
	/**
	 * Some category.
	 * 
	 * @param id
	 */
	public Category categoryOne(int id) {
		return categoryRepository.findOne(id);
	}

}
