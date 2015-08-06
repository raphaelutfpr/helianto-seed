package org.helianto.security.controller;

import org.helianto.security.internal.UserAuthentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Logout controller.
 * 
 * @author mauriciofernandesdecastro
 */
@Controller
@RequestMapping(value={"/logout"})
public class LogoutController {

	/**
	 * Logout page.
	 * 
	 * GET		/logout
	 */
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value={"/", " "}, method= RequestMethod.GET)
	public String logout(UserAuthentication userAuthentication) {
		SecurityContextHolder.clearContext();	
		return "redirect:/";
	}
	
}
