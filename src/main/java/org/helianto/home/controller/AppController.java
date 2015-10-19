package org.helianto.home.controller;

import java.util.Locale;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
	 * @param locale
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.GET)
	public String empty(Model model, Locale locale) {
		return all(model, "home", locale, null);
	}

	/**
	 * Generic menu.
	 * 
	 * @param model
	 * @param baseName
	 * @param locale
	 * @param externalId optional
	 */
	@RequestMapping(value={"/{baseName}", "/{baseName}/"}, method=RequestMethod.GET)
	public String all(Model model, @PathVariable String baseName, Locale locale
			, @RequestParam(required=false) Integer externalId) {
		return response(model, baseName, locale, externalId);
	}

	/**
	 * Default response.
	 * 
	 * @param model
	 * @param baseName
	 * @param externalId
	 */
	protected String response(Model model, String baseName, Locale locale, Integer externalId) {
		model.addAttribute("baseName", baseName);
		if (externalId!=null) {
			model.addAttribute("externalId", externalId);
		}
		if (locale!=null) {
			model.addAttribute("locale", locale.toString().toLowerCase());
			model.addAttribute("locale_", locale.toString().replace("_", "-").toLowerCase());
		}
		System.err.println("//////"+locale.toString());
		return getTemplateName();
	}
	
	/**
	 * Default template name.
	 */
	protected String getTemplateName() {
		return "frame-bootstrap";
	}
	
}
