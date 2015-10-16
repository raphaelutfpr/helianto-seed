package org.helianto.security.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.helianto.core.domain.Signup;
import org.helianto.core.sender.UserConfirmationSender;
import org.helianto.security.service.SignupService;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;


/**
 * Base classe to SignUpController.
 * 
 * @author mauriciofernandesdecastro
 */
@Controller
@RequestMapping(value="/signup")
public class SignUpController 
	extends AbstractCryptoController
{

	public static final String SIGN_UP = "/signup";
	
	public static final String SIGN_UP_TEMPLATE = "security/signup";
	
	public static final String WELCOME_TEMPLATE = "security/welcome";
	
	@Inject
	private Environment env;
	
	@Inject
	private SignupService signupService;

	@Inject 
	private UserConfirmationSender userConfirmationSender;
	
	/**
	 * Send user confirmation e-mail.
	 * 
	 * @param signup
	 */
	public String sendConfirmation(Signup signup) {
		System.err.println("Signup: " + signup.getPrincipal());

		if (userConfirmationSender.send(signup.getPrincipal(), signup.getFirstName(), signup.getLastName(), "Email Confirmação", "confirmationToken", signup.getToken())) {
			return "true";
		}
		return "false";
	}
	
	/**
	 * Signup request.
	 * 
	 * @param model
	 * @param contextId
	 * @param principal
	 * @param request
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.GET)
	public ModelAndView getSignupPage(Model model, @RequestParam(defaultValue="1") Integer contextId
			, @RequestParam(required=false) String principal, WebRequest request) {
		if(signupService.notifyAdminIfUserIsNotValid(contextId, principal)){
			return new ModelAndView("forward:/");
		}
		Signup signup = signupService.socialSignUpAttempt(contextId, request);
		return new ModelAndView(SIGN_UP_TEMPLATE, "form", signup);
	}
	
	/**
	 * Check if email exists.
	 * 
	 * @param model
	 * @param principal
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.GET, params="principal", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String verify(@RequestParam String principal) {
		return "{\"exists\":" + !signupService.searchPrincipal(new Signup(0, principal)) + "}";
	}
	
	/**
	 * Save Lead
	 * @param principal
	 * @return
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.POST, params="tempEmail", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String saveLead(@RequestParam String tempEmail) {
		if(signupService.saveLead(tempEmail)){
			return "{\"exists\": true}";
		}
		return "{\"exists\": false}";
	}
	

	/**
	 * Check if email exists.
	 * 
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.POST, params="emailChecked", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String checkMail(@RequestBody Signup form) {
		return "{\"notExists\":" + signupService.searchPrincipal(form) + "}";
	}
	
	/**
	 * Signup submission
	 * 
	 * @param model
	 * @param signup
	 * @param error
	 * @param request
	 */
	@RequestMapping(value={"/", ""}, method= RequestMethod.POST)
	public String postSignupPage(Model model, @Valid Signup signup, BindingResult error, HttpServletRequest request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");  
		if (ipAddress == null) {  
			  ipAddress = request.getRemoteAddr();  
		}
		// TODO prevent double submission
		signup.setToken(signupService.createToken());
		signup = signupService.saveSignup(signup, ipAddress);
		boolean userExists = signupService.allUsersForIdentityAreValid(signup);
		model.addAttribute("userExists", userExists);

		if (userExists) {
			model.addAttribute("sender", env.getProperty("iservport.sender.mail"));
			model.addAttribute("emailSent", sendConfirmation(signup));
		}
		model.addAllAttributes(signup.createMapFromForm());
		return WELCOME_TEMPLATE;
	}
	
	// TODO
	public void resendConfirmation() {
		
	}
	
}
