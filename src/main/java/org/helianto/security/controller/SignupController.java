package org.helianto.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Signup controller.
 * 
 * @author mauriciofernandesdecastro
 */
@Controller
@RequestMapping(value={"/signup"})
public class SignupController {
	
	/**
	 * Signup page.
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.GET)
	public String signin( String error, Model model, @RequestParam(required = false) String logout ) {
		model.addAttribute("baseName", "security");
		if (error!=null && error.equals("1")) {
			model.addAttribute("error", "1");
		}
		return "/security/signup";
	}

	/**
	 * License page.
	 */
	@RequestMapping(value={"/license"}, method=RequestMethod.GET)
	public String license() {
		return "/security/license";
	}
	
	/**
	 * Privacy policy page.
	 */
	@RequestMapping(value={"/privacy"}, method=RequestMethod.GET)
	public String privacy() {
		return "/security/privacy";
	}
}
