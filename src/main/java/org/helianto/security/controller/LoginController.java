package org.helianto.security.controller;

import javax.inject.Inject;

import org.helianto.core.domain.Entity;
import org.helianto.core.repository.EntityRepository;
import org.helianto.security.internal.UserAuthentication;
import org.helianto.user.domain.User;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Login controller.
 * 
 * @author mauriciofernandesdecastro
 */
@Controller
@RequestMapping(value={"/login"})
public class LoginController {
	
	public static final String LOGIN_TEMPLATE = "security/login";
	
	@Inject 
	private Environment env;
	
	@Inject 
	private EntityRepository entityRepository;
	
	/**
	 * Login page.
	 */
	@RequestMapping(method=RequestMethod.GET)
	public String signin( String error, Model model, @RequestParam(required = false) String logout ) {
		model.addAttribute("baseName", "security");
		model.addAttribute("main", "security/login");
		model.addAttribute("copyright", env.getProperty("helianto.copyright", ""));
		if (error!=null && error.equals("1")) {
			model.addAttribute("error", "1");
		}
		return "frame-security";
	}

	/**
	 * Login errors.
	 * 
	 * @param model
	 * @param username
	 */
	@RequestMapping(value="/error", method=RequestMethod.GET)
	public String loginError( Model model, @RequestParam String type) {
		model.addAttribute("baseName", "security");
		model.addAttribute("main", "security/login");
		model.addAttribute("error", true);
		User user = new User();
		user.setAccountNonExpired(false);
		model.addAttribute("user", user);
		return "frame-security";
	}
	
	/**

	 * Logout page.
	 * 
	 * GET		/logout
	 * @deprecated
	 */
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value="/logout",method= RequestMethod.GET)
	public String logout(UserAuthentication userAuthentication) {
		SecurityContextHolder.clearContext();	
		return "redirect:/";
	}
	
	/**
	 * Logo.
	 * 
	 * @param model
	 */
	@RequestMapping(value="/logo", method=RequestMethod.GET)
	@ResponseBody
	public String logo() {
		String logo = "/images/logo/logo-iserv.png";
	
		Entity entity = entityRepository.findOne(1);
		if (entity!=null && !entity.getExternalLogoUrl().isEmpty()) {
			logo =  entity.getExternalLogoUrl();
		}
		
		return "{\"logo\":\""+logo+"\"}";
	}
	
	
}
