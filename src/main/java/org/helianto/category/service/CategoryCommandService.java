package org.helianto.category.service;

import javax.inject.Inject;

import org.helianto.core.domain.Category;
import org.helianto.core.domain.Entity;
import org.helianto.core.repository.CategoryRepository;
import org.helianto.core.repository.EntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Category command service.
 * 
 * @author Eldevan Nery Junior
 */
@Service
public class CategoryCommandService {

	private static final Logger logger = LoggerFactory.getLogger(CategoryCommandService.class);
	
	@Inject 
	private CategoryRepository categoryRepository;
	
	@Inject 
	private EntityRepository entityRepository;
	
	/**
	 * Save or update category.
	 * 
	 * @param entityId
	 * @param command
	 */
	public Category category(int entityId, Category command) {
		Category target = null;
		if(command.getId()==0){
			Integer existing 
			= categoryRepository.findByEntity_IdAndCategoryGroupAndCategoryCode(entityId
				, command.getCategoryGroup() 
				, command.getCategoryCode());
			if(existing!=null && existing>0){
				logger.debug("Category code={} already exists.", command.getCategoryCode());
				throw new RuntimeException();
			}
			Entity entity = entityRepository.findOne(entityId);
			target = new Category(entity, command.getCategoryGroup(), command.getCategoryCode());
		}
		else{
			target = categoryRepository.findOne(command.getId());
		}
		Category categoryToFlush = target.merge(command);
		return categoryRepository.saveAndFlush(categoryToFlush);
	}
	
}
