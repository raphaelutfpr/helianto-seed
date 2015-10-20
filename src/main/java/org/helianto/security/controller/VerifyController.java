/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helianto.security.controller;

import java.util.List;

import javax.inject.Inject;

import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Identity;
import org.helianto.core.domain.Lead;
import org.helianto.core.domain.Operator;
import org.helianto.core.domain.Signup;
import org.helianto.core.repository.IdentityRepository;
import org.helianto.core.repository.LeadRepository;
import org.helianto.core.repository.OperatorRepository;
import org.helianto.core.repository.SignupRepository;
import org.helianto.install.service.EntityInstallStrategy;
import org.helianto.install.service.UserInstallService;
import org.helianto.security.domain.IdentitySecret;
import org.helianto.security.internal.UserDetailsAdapter;
import org.helianto.security.repository.SignupTmpRepository;
import org.helianto.security.service.AuthorizationChecker;
import org.helianto.security.util.SignInUtils;
import org.helianto.user.domain.User;
import org.joda.time.DateMidnight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

/**
 * Base class to verify controllers.
 * 
 * Subclasses must implement the prototype generation strategy accordingly.
 * 
 * @author mauriciofernandesdecastro
 */
@Controller
@RequestMapping("/verify")
public class VerifyController
	extends AbstractCryptoController
{
	
	private static final Logger logger = LoggerFactory.getLogger(VerifyController.class);
	
	//public static final String PWD_CREATE = "/security/passwordCreate";
	
	public static final String PWD_VERIFY = "/security/password";
	
	@Inject
	private Environment env;
	
	@Inject 
	private OperatorRepository contextRepository;
	
	@Inject 
	private IdentityRepository identityRepository;
	
	@Inject
	private LeadRepository leadRepository;
	
	@Inject 
	private UserInstallService userInstallService;
	
	@Inject
	private EntityInstallStrategy entityInstallStrategy;
	
	@Inject
	private SignupRepository signupRepository;
	
	@Inject
	private AuthorizationChecker authorizationChecker;
	
	@Inject
	private SignupTmpRepository signupTmpRepository;

	@Inject
	private UsersConnectionRepository connectionRepository;
	
	@Inject
	private ConnectionFactoryLocator connectionFactoryLocator;

	/**
	 * Create = true if identity not yet exists.
	 * 
	 * @param principal
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.GET, params={"principal"})
	@ResponseBody
	public String exists(@RequestParam String principal) {
		Identity identity = identityRepository.findByPrincipal(principal);
		return String.format("{\"canCreate\":%b}", identity==null);
	}

	/**
	 * Verify token received after user confirmation e-mail.
	 * 
	 * @param model
	 * @param identityId
	 */
	@RequestMapping(value={"/", ""}, method= RequestMethod.GET, params={"confirmationToken"})
	public String getVerificationPage(Model model, @RequestParam String confirmationToken) {
		int identityId = findPreviousSignupAttempt(confirmationToken, 5);
		if (identityId!=0) {
			Identity  identity = identityRepository.findOne(identityId);
			return createPassword(model, identity);
		}
		else {
			model.addAttribute("userConfirmed", false);
		}
		return SignUpController.SIGN_UP;
	}
	
	/**
	 * Find a valid previous signup attempt.
	 * 
	 * @param confirmationToken
	 * @param expirationLimit
	 */
	protected int findPreviousSignupAttempt(String confirmationToken, int expirationLimit) {
		Signup signup = signupTmpRepository.findByToken(confirmationToken);
		if (signup!=null) {
			if (expirationLimit>0 && signup.getIssueDate()!=null) {
				DateMidnight expirationDate = new DateMidnight(signup.getIssueDate()).plusDays(expirationLimit + 1);
				logger.debug("Previous signup attempt valid to {} ", expirationDate);
				if (expirationDate.isAfterNow()) {
					return identityRepository.findByPrincipal(signup.getPrincipal()).getId();
				}
			}
		}
		logger.debug("Unable to detect any valid previous signup attempt with token {} ", confirmationToken);
		return 0;
	}
	
	/**
	 * Authorize.
	 * 
	 * @param user
	 * @param request
	 */
	public void authorize(User user, WebRequest request) {
		if (user != null) {
			UserDetailsAdapter userDetails = authorizationChecker.updateAuthorities(new UserDetailsAdapter(user));
			SignInUtils.signin(userDetails);
			ProviderSignInUtils providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
			providerSignInUtils.doPostSignUp(user.getId()+"", request);
		}
	}
	
	/**
	 * Create password after successful verification.
	 * 
	 * @param model
	 * @param identityId
	 */
	@RequestMapping(value="/password", method= RequestMethod.GET)
	public String createPassword(Model model, @RequestParam String token) {
		int identityId = decriptAndValidateToken(token);
		Identity  identity = identityRepository.findOne(identityId);
		return createPassword(model, identity);
	}
	
	/**
	 * Internal create password.
	 * 
	 * @param model
	 * @param identity
	 */
	protected String createPassword(Model model, Identity  identity) {
		model.addAttribute("titlePage", "Password creation");
		model.addAttribute("baseName", "security");
		model.addAttribute("main", "security/passwordChange");
		model.addAttribute("copyright", env.getProperty("helianto.copyright", ""));
		
		if(identity!=null){
			model.addAttribute("email", removeLead(identity.getPrincipal()));
			// prevents duplicated submission
			IdentitySecret identitySecret = getIdentitySecret(identity);			
			if(identitySecret!= null){
				return "redirect:/";
			}
		}
		else{
			return "redirect:"+SignUpController.SIGN_UP;
		}
		return PasswordRecoveryController.FRAME_SECURITY;
	}
	
	/**
	 * Create user and password.
	 * 
	 * @param model
	 * @param email
	 * @param password
	 */
	@RequestMapping(value="/createPass", method= RequestMethod.POST)
	public String postVerificationPage(Model model, @RequestParam(defaultValue="1") Integer contextId
			, @RequestParam String email, @RequestParam String password) {
		
		Identity identity = identityRepository.findByPrincipal(email);
		model.addAttribute("userExists", true);
		logger.debug("User {} exists",identity.getPrincipal());
		IdentitySecret identitySecret = getIdentitySecret(identity);
		if (identitySecret==null) {
			logger.info("Will install identity secret for {}.", identity);
			identitySecret = createIdentitySecret(identity, password);
		}
		Operator context = contextRepository.findOne(contextId);
		Signup signup = getSignup(contextId, identity);
		List<Entity> prototypes = generateEntityPrototypes(signup);
		createEntities(context, prototypes, identity);
		model.addAttribute("passError", "false");
		
		return SignUpController.WELCOME_TEMPLATE;
		
	}
	
	/**
	 * Retrieve previous signup.
	 * 
	 * @param contextId
	 * @param identity
	 */
	protected Signup getSignup(Integer contextId, Identity identity) {
		return signupRepository.findByContextIdAndPrincipal(contextId, identity.getPrincipal());
	}
	
	/**
	 * Generate entity prototypes.
	 * 
	 * @param identity
	 */
	protected List<Entity> generateEntityPrototypes(Signup signup){
		return entityInstallStrategy.generateEntityPrototypes(signup);
	}

	/**
	 * Create entities.
	 * 
	 * @param prototypes
	 * @param identity
	 */
	protected void createEntities(Operator context, List<Entity> prototypes, Identity identity) {
		Entity entity = null;
		for (Entity prototype: prototypes) {
			entity = entityInstallStrategy.installEntity(context, prototype);
			if(entity!=null){
				createUser(entity, identity);
			}
		}
	}
	
	/**
	 * Create new user.
	 * 
	 * @param entity
	 * @param form
	 * @param formBinding
	 */
	protected User createUser(Entity entity, Identity identity) {
		try {
			String principal = identity.getPrincipal();
			User user = userInstallService.installUser(entity,  principal);
			removeLead(principal);
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Remove temporary lead.
	 * 
	 * @param leadPrincipal
	 */
	protected final String removeLead(String leadPrincipal){
		List<Lead> leads = leadRepository.findByPrincipal(leadPrincipal);	
		for (Lead lead : leads) {
			leadRepository.delete(lead);
		}
		return leadPrincipal;
	}
	
}
