package org.helianto.home.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Application menu pages controller.
 * 
 * @author mauriciofernandesdecastro
 */
@Controller
@RequestMapping("/")
@PreAuthorize("isAuthenticated()")
public class AppController {
	
	/**
	 * Menu home.
	 * 
	 * @param model
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.GET)
	public String empty(Model model) {
		return all(model, "home");
	}

	/**
	 * Generic menu.
	 * 
	 * @param model
	 */
	@RequestMapping(value={"/{baseName}", "/{baseName}/"}, method=RequestMethod.GET)
	public String all(Model model, @PathVariable String baseName) {
		return response(model, baseName);
	}

	/**
	 * Default response.
	 * 
	 * @param model
	 * @param baseName
	 */
	protected String response(Model model, String baseName) {
		model.addAttribute("baseName", baseName);
		return "frame-bootstrap";
	}
	
}
