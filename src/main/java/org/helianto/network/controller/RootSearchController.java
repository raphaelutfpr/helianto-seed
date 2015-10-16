package org.helianto.network.controller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.helianto.core.internal.QualifierAdapter;
import org.helianto.home.controller.SearchForm;
import org.helianto.network.service.RootCommandService;
import org.helianto.network.service.RootQueryService;
import org.helianto.security.internal.UserAdapter;
import org.helianto.security.internal.UserAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Entity network controller.
 * 
 * @author mauriciofernandesdecastro
 */
@RequestMapping(value={"/app/root"})
@Controller
@PreAuthorize("isAuthenticated()")
public class RootSearchController {

	private static final Logger logger = LoggerFactory.getLogger(RootSearchController.class);
	
	@Inject
	private RootCommandService rootCommandService;
	
	@Inject
	private RootQueryService rootQueryService;
	
	/**
	 * Qualifiers.
	 *
	 * GET /api/root/qualifier
	 */
	@RequestMapping(value = { "/qualifier" }, method = RequestMethod.GET)
	@ResponseBody
	public List<QualifierAdapter> qualifier(
			UserAuthentication userAuthentication) {
		return rootQueryService.qualifier(userAuthentication.getEntityId());
	}

	/**
	 * Entities.
	 *
	 * GET /api/root/entity?entityType
	 */
	@RequestMapping(value ="/entity", method = RequestMethod.GET, params={"entityType"})
	@ResponseBody
	public Page<UserAdapter> entities(UserAuthentication userAuthentication, @RequestParam Character entityType) {
		return rootQueryService.listUser(userAuthentication.getIdentityId(), entityType);
	}

	/**
	 * Entity.
	 * 
	 * GET /api/root/entity/?userId
	 */
	@RequestMapping(value ="/entity", method = RequestMethod.GET, params={"userId"})
	@ResponseBody
	public UserAdapter entity(UserAuthentication userAuthentication, @RequestParam Integer userId) {
		return rootQueryService.entity(userId);
	}
	
	/**
	 * Search entity.
	 * 
	 * @param userAuthentication
	 * @param model
	 * @param searchString
	 */
	@RequestMapping(value="/search", method= RequestMethod.POST)
	@ResponseBody
	public Page<UserAdapter> search(UserAuthentication userAuthentication, @RequestParam(defaultValue="0") Integer pageNumber
			, @RequestBody SearchForm form) {
		return rootQueryService.search(userAuthentication.getEntityId(), form.getSearchString(), pageNumber);
	}
	
	/**
	 * Authorize user.
	 * 
	 * GET /api/root/entity?rootId
	 * 
	 * @param userAuthentication
	 * @param model
	 * @param searchString
	 */
	@RequestMapping(value="/entity", method= RequestMethod.GET, params={"rootUserId"})
	@ResponseBody
	public String authorize(UserAuthentication userAuthentication, HttpServletRequest request
			, HttpServletResponse response, @RequestParam Integer rootUserId) {
		if(rootCommandService.authorize(rootUserId, userAuthentication.getUserId())) {
			cancelRemeberMeCookie(request, response);
			logger.debug("User authorized.");
			return "{\"success\":1}";
		}
		return null;
	}
	
	/**
	 * Cancel remember-me cookie.
	 * 
	 * @param request
	 * @param response
	 */
	protected void cancelRemeberMeCookie(HttpServletRequest request, HttpServletResponse response) {
		String cookieName = AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY;
		  Cookie cookie = new Cookie(cookieName, null);
		  cookie.setMaxAge(0);
		  cookie.setPath(StringUtils.hasLength(request.getContextPath()) ? request.getContextPath() : "/");
		  response.addCookie(cookie);
	}
	
}
