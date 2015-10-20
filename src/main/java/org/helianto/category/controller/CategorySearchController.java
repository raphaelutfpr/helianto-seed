package org.helianto.category.controller;

import java.util.List;

import javax.inject.Inject;

import org.helianto.category.service.CategoryCommandService;
import org.helianto.category.service.CategoryQueryService;
import org.helianto.core.def.CategoryGroup;
import org.helianto.core.domain.Category;
import org.helianto.core.internal.QualifierAdapter;
import org.helianto.home.controller.SearchForm;
import org.helianto.security.internal.UserAuthentication;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Category controller.
 * 
 * @author mauriciofernandesdecastro
 */
@RequestMapping(value={"/api/category"})
@RestController
@PreAuthorize("isAuthenticated()")
public class CategorySearchController {

	@Inject
	private CategoryQueryService categoryQueryService;
	
	@Inject 
	private CategoryCommandService categoryCommandService;
	
	/**
	 * List qualifiers.
	 *
	 * GET		/app/category/qualifier
	 */
	@RequestMapping(value={"/qualifier"}, method=RequestMethod.GET)
	public List<QualifierAdapter> qualifier(UserAuthentication userAuthentication) {
		return categoryQueryService.qualifier(userAuthentication.getEntityId());
	}
	
	/**
	 * List categories by group type.
	 *
	 * GET       /app/category?categoryGroupType
	 */
	@RequestMapping(method=RequestMethod.GET, params={"categoryGroupType"})
	public Page<Category> categoryList(UserAuthentication userAuthentication
			, @RequestParam CategoryGroup categoryGroupType 
			, @RequestParam(defaultValue="0")Integer pageNumber) {
		return categoryQueryService.category(userAuthentication.getEntityId(), categoryGroupType, pageNumber);
	}
	
	/**
	 * Some category.
	 *
	 * GET       /app/category?categoryId
	 */
	@RequestMapping(method=RequestMethod.GET, params={"categoryId"})
	public Category category(@RequestParam Integer categoryId) {
		return categoryQueryService.categoryOne(categoryId);
	}
	
	/**
	 * New category in the group.
	 *
	 * POST       /app/category?categoryGroupType
	 */
	@RequestMapping(method=RequestMethod.POST, params={"categoryGroupType"})
	public Category categoryNew(UserAuthentication userAuthentication
			, @RequestParam CategoryGroup categoryGroupType) {
		Category category = new Category();
		category.setCategoryGroupType(categoryGroupType);
		return category;
	}
	
	/**
	 * Search category.
	 *
	 * POST       /app/category/search
	 */
	@RequestMapping(value={"/search"},method=RequestMethod.POST)
	public Page<Category> categorySearch(UserAuthentication userAuthentication
			, @RequestBody SearchForm command) {
		return categoryQueryService.categorySearch(userAuthentication.getEntityId() 
				, command.getQualifierValueChar()
				, command.getPageNumber(), command.getSearchString()
				, command.getOrderString());
	}
	
	
	/**
	 * Update category.
	 *
	 * PUT       /app/category
	 */
	@RequestMapping(method=RequestMethod.PUT, consumes="application/json")
	public Category category(UserAuthentication userAuthentication
			, @RequestBody Category command) {
		return categoryCommandService.category(userAuthentication.getEntityId(), command);
	}
	
}
