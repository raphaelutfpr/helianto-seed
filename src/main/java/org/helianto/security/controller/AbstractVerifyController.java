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
import org.helianto.core.repository.IdentityRepository;
import org.helianto.core.repository.LeadRepository;
import org.helianto.install.service.EntityInstallService;
import org.helianto.install.service.EntityInstallStrategy;
import org.helianto.install.service.UserInstallService;
import org.helianto.security.domain.IdentitySecret;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Verify controller.
 * 
 * @author mauriciofernandesdecastro
 */
@RequestMapping("/verify")
public abstract class AbstractVerifyController
	extends AbstractCryptoController
{
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractVerifyController.class);
	
	public static final String PWD_CREATE = "/login/createPassword";
	
	public static final String PWD_VERIFY= "/signup/password";
	
	public static final String HOME = "redirect:/";
	
	public static final String SIGN_UP = "/login/signup/";
	
	@Inject 
	private IdentityRepository identityRepository;
	
	@Inject
	private LeadRepository leadRepository;
	
	@Inject 
	private UserInstallService userInstallService;
	
	@Inject
	private EntityInstallService entityInstallService;
	
	@Inject
	private EntityInstallStrategy entityInstallStrategy;
	
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
	public String verify(Model model, @RequestParam String confirmationToken) {
		int identityId = decriptAndValidateToken(confirmationToken);
		if (identityId!=0) {
			Identity  identity = identityRepository.findOne(identityId);
			logger.debug("Identity verified {} ", identity.getPrincipal());
			return "redirect:"+PWD_VERIFY+"?token="+encryptToken(identity);
		}
		else {
			model.addAttribute("userConfirmed", false);
			logger.debug("Unabble to verify identity {} ", identityId);
			return SIGN_UP;
		}
	}
	
	/**
	 * Create password after successful verification.
	 * 
	 * @param model
	 * @param identityId
	 */
	@RequestMapping(value="/password", method= RequestMethod.GET)
	public String password(Model model, @RequestParam String token) {
		int identityId = decriptAndValidateToken(token);
		Identity  identity = identityRepository.findOne(identityId);
		if(identity!=null){
			model.addAttribute("email", removeLead(identity.getPrincipal()));
			// prevents duplicated submission
			IdentitySecret identitySecret = getIdentitySecret(identity);			
			if(identitySecret!= null){
				return "redirect:"+HOME;
			}
		}
		else{
			return "redirect:"+SIGN_UP;
		}
		return PWD_CREATE;
	}
	
	/**
	 * Create user.
	 * 
	 * @param model
	 * @param email
	 * @param password
	 */
	@RequestMapping(value="/createPass", method= RequestMethod.POST)
	public String createPass(Model model, @RequestParam String email, @RequestParam String password) {
		
		Identity identity = identityRepository.findByPrincipal(email);
		model.addAttribute("userExists", true);
		logger.debug("User {} exists",identity.getPrincipal());
		IdentitySecret identitySecret = getIdentitySecret(identity);
		if (identitySecret==null) {
			logger.info("Will install identity secret for {}.", identity);
			identitySecret = createIdentitySecret(identity, password);
		}	
		//TODO verify : install user on entity one by default?
		List<Entity> prototypes = entityInstallStrategy.generateEntityPrototypes(identity);
		Entity entity = null;
		for (Entity prototype: prototypes) {
			entity = entityInstallService.installEntity(prototype);
		}
		if(entity!=null){
			userInstallService.installUser(entity, identity.getPrincipal());
		}
		model.addAttribute("passError", "false");
		
		return "login/welcome";
		
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
