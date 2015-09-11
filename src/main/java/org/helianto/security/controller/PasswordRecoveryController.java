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
 * Controlador para o caso de uso de recuperação de senha.
 * 
 * @author mauriciofernandesdecastro
 */
@RequestMapping(value="/recovery")
public class PasswordRecoveryController {
	
	@Inject 
	private IdentityRepository identityRepository;
	
	@Inject
	private Environment env;
	
	@Inject
	private AbstractCryptoController identityCrypto;
	
	@Inject 
	private UserRepository userRepository;
	
	@Inject
	private IdentitySecretRepository identitySecretRepository;
	
	
	/**
	 * Obtém e-mail para recuperação de senha.
	 */
	@RequestMapping(value={"/", ""}, method={ RequestMethod.GET })
	public String recovery(String error, Model model) {
		
		return "login/passwordRecover";
		
	}
		
	/**
	 * Formulário de troca de senha (usuários já autenticados).
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
				return "login/passwordChange";
				
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
	 * Recebe formulário com a nova senha.
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
			identityCrypto.changeIdentitySecret(identity.getPrincipal(), password);
			model.addAttribute("recoverFail", "false");
			return "redirect:/app/home/";
		}
		model.addAttribute("recoverFailMsg", "label.user.password.recover.fail.message.1");
		model.addAttribute("recoverFail", "true");
		
		return "login/passwordChange";
	}

	/**
	 * ENVIA o email com os dados para recuperação de senha.
	 * 
	 * @param model
	 * @param principal
	 */
	@RequestMapping(value="/send", method= {RequestMethod.POST, RequestMethod.GET })
	public String send(Model model, @RequestParam(required=false) String principal) {
		
		if (principal==null) {
			model.addAttribute("recoverFailMsg", "label.user.password.recover.fail.message.1");
			model.addAttribute("recoverFail", "true");
			return "login/passwordRecover";
		}
		
		try {
			Identity identity = identityRepository.findByPrincipal(principal);
			
			if (identity!=null) {
				Identity sender = identityRepository.findByPrincipal(env.getProperty("iservport.sender.mail"));
				
				// Envia...
				/*
				if (UserPasswordRecoverySender.send(identity, sender, env, identityCrypto.encryptToken(identity))) {
					model.addAttribute("emailRecoverySent", true);
				}
				else {
					// Caso falhe o envio, retorna ao formulário de e-mail
					model.addAttribute("emailRecoveryFailed", true);
					return "/login/passwordRecover";
				} 
				*/
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
		System.err.println("Token:   " + token);
		int identityId = identityCrypto.decriptAndValidateToken(token);
		Identity identity = identityRepository.findOne(identityId);
		if (identity!=null) {
			model.addAttribute("email", identity.getPrincipal());
			return "login/passwordChange";
			
		}
		model.addAttribute("recoverFailMsg", "label.user.password.recover.fail.message.1");
		model.addAttribute("recoverFail", "true");
		
		return "login/passwordChange";
	}
	
}
