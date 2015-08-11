package org.helianto.user.controller;

import java.util.List;

import javax.inject.Inject;

import org.helianto.core.internal.QualifierAdapter;
import org.helianto.security.internal.UserAuthentication;
import org.helianto.seed.PageDecorator;
import org.helianto.user.domain.User;
import org.helianto.user.service.UserCommandService;
import org.helianto.user.service.UserQueryService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * User controller.
 * 
 * @author mauriciofernandesdecastro
 */
@Controller
@RequestMapping("/api/user")
@PreAuthorize("isAuthenticated()")
public class UserController {

	@Inject
	private UserQueryService userQueryService;
	
	@Inject
	private UserCommandService userCommandService;
	
	/**
	 * List qualifiers.
	 * 
	 * GET		/api/user/qualifier
	 */
	@RequestMapping(value={"/qualifier"}, method=RequestMethod.GET)
	@ResponseBody                                                           
	public List<QualifierAdapter> qualifier(UserAuthentication userAuthentication) {
		return userQueryService.qualifierList(userAuthentication.getEntityId());
	}
	
	/**
	 * List users.
	 * 
	 * GET		/api/user/?userType&userStates&pageNumber&itemsPerPage
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.GET, params={"userType", "userStates"})
	@ResponseBody                                                           
	public Page<User> userList(UserAuthentication userAuthentication, @RequestParam Character userType
			, @RequestParam String userStates, @RequestParam(defaultValue="1") Integer pageNumber
			, @RequestParam(defaultValue="20") Integer itemsPerPage) {
		Page<User> userList = new PageDecorator<User>(
				userQueryService.userList(userAuthentication.getEntityId(), userType, userStates, pageNumber - 1, itemsPerPage));
		return userList;
	}
		
	/**
	 * List users.
	 * 
	 * GET		/api/user/?userType&userStates&pageNumber&itemsPerPage
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.GET, params={"userId"})
	@ResponseBody                                                           
	public User user(UserAuthentication userAuthentication, @RequestParam Integer userId) {
		return userQueryService.user(userAuthentication.getEntityId(), userId);
	}
	
}
