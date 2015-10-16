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

import javax.inject.Inject;

import org.helianto.core.domain.Identity;
import org.helianto.core.repository.IdentityRepository;
import org.helianto.core.sender.PasswordRecoverySender;
import org.helianto.security.domain.IdentitySecret;
import org.helianto.security.internal.UserAuthentication;
import org.helianto.security.repository.IdentitySecretRepository;
import org.helianto.user.domain.User;
import org.helianto.user.repository.UserRepository;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Spring MVC password recovery controller.
 * 
 * @author mauriciofernandesdecastro
 */
@Controller
//@RequestMapping(value="/recovery")
public class PasswordRecoveryController extends AbstractCryptoController{
	
	@Inject 
	private IdentityRepository identityRepository;
	
	@Inject
	private Environment env;
	
	@Inject 
	private UserRepository userRepository;
	
	@Inject
	private IdentitySecretRepository identitySecretRepository;
	
	@Inject
	private PasswordRecoverySender passwordRecoverySender;
	
	
	/**
	 * Password recovery e-mail.
	 */
	@RequestMapping(value={"/recovery", ""}, method={ RequestMethod.GET })
	public String recovery(String error, Model model) {
		return "security/passwordRecover";
		
	}
		
	/**
	 * Password change form (when user is already registered).
	 * 
	 * @param userAuthentication
	 * @param model
	 */
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value={"/self", "self"}, method= {RequestMethod.POST, RequestMethod.GET })
	public String change(UserAuthentication userAuthentication, Model model) {
		try {
			User user = userRepository.findOne(userAuthentication.getUserId());
			Identity  identity = user.getIdentity();
			if (identity!=null) {
				model.addAttribute("email", identity.getPrincipal());
				return "security/passwordChange";
				
			}
			model.addAttribute("recoverFailMsg", "label.user.password.recover.fail.message.1");
			model.addAttribute("recoverFail", "true");
		} catch (Exception e) {
			model.addAttribute("recoverFailMsg", e.getMessage());
			model.addAttribute("recoverFail", "true");
		}
		
		return "redirect:/login/";
		
	}
	
	/**
	 * Password change submission.
	 * 
	 * @param model
	 * @param email
	 * @param password
	 */
	@RequestMapping(value="/submit", method= RequestMethod.POST)
	public String recover(Model model, @RequestParam String email, @RequestParam String password) {
		
		Identity identity = identityRepository.findByPrincipal(email);
		if (identity!=null) {
			IdentitySecret identitySecret = identitySecretRepository.findByIdentityKey(identity.getPrincipal());
			model.addAttribute("email", identity.getPrincipal());
			//verifica se a senha não é a mesma
			if(BCrypt.checkpw(password, identitySecret.getIdentitySecret())){
				model.addAttribute("recoverFail", "true");
				model.addAttribute("recoverFailMsg", "label.user.password.recover.fail.message.0");
				return "login/passwordChange";
			}
			changeIdentitySecret(identity.getPrincipal(), password);
			model.addAttribute("recoverFail", "false");
			return "redirect:/app/home/";
		}
		model.addAttribute("recoverFailMsg", "label.user.password.recover.fail.message.1");
		model.addAttribute("recoverFail", "true");
		
		return "security/passwordChange";
	}

	/**
	 * Send password retrieval e-mail.
	 * 
	 * @param model
	 * @param principal
	 */
	@RequestMapping(value="/send", method= {RequestMethod.POST, RequestMethod.GET })
	public String send(Model model, @RequestParam(required=false) String principal) {
		
		if (principal==null) {
			model.addAttribute("recoverFailMsg", "label.user.password.recover.fail.message.1");
			model.addAttribute("recoverFail", "true");
			return "security/passwordRecover";
		}
		
		try {
			Identity recipient = identityRepository.findByPrincipal(principal);
			
			if (recipient!=null) {
				if (passwordRecoverySender.send(recipient, env.getProperty("sender.recovery.subject", "Password recovery e-mail"))) {
					model.addAttribute("emailRecoverySent", true);
				}
				else {
					// Caso falhe o envio, retorna ao formulário de e-mail
					model.addAttribute("emailRecoveryFailed", true);
					return "/security/passwordRecover";
				} 
				return "redirect:/login/";
			}
			
			model.addAttribute("recoverFailMsg", "label.user.password.recover.fail.message.1");
			model.addAttribute("recoverFail", "true");
			
		} catch (Exception e) {
			
			model.addAttribute("recoverFailMsg", e.getMessage());
			model.addAttribute("recoverFail", "true");
			
		}
		
		return "redirect:/login/";
		
	}
	
	/**
	 * RECEBE a confirmação do e-mail e apresenta formulário para troca de senha (usuários ainda não autenticados).
	 * 
	 * @param model
	 * @param email
	 * @param identityId
	 */
	@RequestMapping(value="/return/{token}", method=RequestMethod.GET)
	public String mail(Model model, @PathVariable String token) {
		int identityId = decriptAndValidateToken(token);
		Identity identity = identityRepository.findOne(identityId);
		if (identity!=null) {
			model.addAttribute("email", identity.getPrincipal());
			return "security/passwordChange";
			
		}
		model.addAttribute("recoverFailMsg", "label.user.password.recover.fail.message.1");
		model.addAttribute("recoverFail", "true");
		
		return "security/passwordChange";
	}
	
}
