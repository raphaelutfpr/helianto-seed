package org.helianto.security.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.helianto.core.domain.Identity;
import org.helianto.core.domain.Lead;
import org.helianto.core.repository.IdentityRepository;
import org.helianto.core.repository.LeadRepository;
import org.helianto.core.sender.NotificationSender;
import org.helianto.user.domain.User;
import org.helianto.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Base classe to SignUpController.
 * 
 * @author mauriciofernandesdecastro
 */
public abstract class AbstractSignUpController 
	extends AbstractCryptoController
{

	private static final Logger logger = LoggerFactory.getLogger(AbstractSignUpController.class);
	
	@Inject
	private Environment env;

	@Inject 
	private IdentityRepository identityRepository;
	
	@Inject 
	private UserRepository userRepository;
	
	@Inject
	private NotificationSender notificationSender;
	
	@Inject
	private LeadRepository leadRepository;
	
	/**
	 * Send user confirmation e-mail.
	 * 
	 * @param identity
	 */
	protected abstract String sendConfirmation(Identity identity);
	
	/**
	 * Inicia o processo já conhecendo o email do solicitante.
	 * 
	 * @param model
	 * @param principal
	 */
	@RequestMapping(value={"/", ""}, method={RequestMethod.GET, RequestMethod.POST})
	public String signin(Model model, @RequestParam(required=false) String principal) {
		Identity identity =null;
		if(principal!=null && principal.length()>0){
			identity = identityRepository.findByPrincipal(principal);
		}
		if(identity!=null){
			return "forward:/";
		}
		if (principal!=null) {
			//verifica se o email é válido
			model.addAttribute("email", principal);
		}
		return "login/signup";
	}
	
	/**
	 * Trata a submissão do formulário de signup.
	 * 
	 * @param model
	 * @param principal
	 * @param firstName
	 * @param lastName
	 * @param password
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/insert", method= RequestMethod.POST)
	public String insert(Model model, @RequestParam String principal, 
			@RequestParam String firstName, @RequestParam String lastName, 
			@RequestParam String domain, @RequestParam Boolean licenseAccepted,
			HttpServletRequest request) throws UnsupportedEncodingException {
		if(!licenseAccepted){
			model.addAttribute("licenseNonAccepted", true);
			model.addAttribute("principal", principal);
			model.addAttribute("firstName", firstName);
			model.addAttribute("lastName", lastName);
			model.addAttribute("domain", domain);
			tempEmail(principal);
			return "login/signup";	
		}
		Identity identity = identityRepository.findByPrincipal(principal);
		
		String ipAddress = request.getHeader("X-FORWARDED-FOR");  
		if (ipAddress == null) {  
			  ipAddress = request.getRemoteAddr();  
		}
		Identity sender = identityRepository.findByPrincipal(env.getProperty("iservport.sender.mail"));
		if (sender==null) {
			throw new IllegalArgumentException("Sender is null.");
		}
		model.addAttribute("sender", sender.getPrincipal());
		
		// Testando se houve um registro anterior bem sucedido.
		if (identity!=null) {
			List<User> userList = userRepository.findByIdentityIdOrderByLastEventDesc(identity.getId());
			
			boolean isActive = false;
			for (User user: userList) {
				if (user.isAccountNonExpired()) {
					isActive = true;
					break;
				}
			}
			if (isActive) {
				model.addAttribute("userExists", true);
				logger.debug("User {} exists",identity.getPrincipal());
			}
			else {
				notificationSender.send(identity.getPrincipal(), identity.getIdentityFirstName(), identity.getIdentityLastName(), "Usuário inativo");
				logger.debug("User {} inactive, notification sent", identity.getPrincipal());
			}
			return "redirect:/";
			
		}
		
		identity= new Identity(principal);
		identity.setDisplayName(firstName);
		identity.getPersonalData().setFirstName(firstName);
		identity.getPersonalData().setLastName(lastName);
		identity.setOptionalSourceAlias(domain);
		identity = identityRepository.saveAndFlush(identity);
		logger.debug("Identity {} created", identity.getPrincipal());
		
		model.addAttribute("emailSent", sendConfirmation(identity));
		model.addAttribute("principal", principal);
		model.addAttribute("firstName", firstName);
		model.addAttribute("lastName", lastName);
		
		return "login/welcome";
		
	}
	
	/**
	 * Captura temporariamente o email do usuário.
	 * 
	 * @param model
	 * @param tempEmail
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.GET, params="tempEmail", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String tempEmail(@RequestParam String tempEmail) {
		Identity identity = identityRepository.findByPrincipal(tempEmail);
		List<Lead> identityTemps = leadRepository.findByPrincipal(tempEmail);
		if(identity==null && (identityTemps==null || identityTemps.size()==0)){
			leadRepository.save(new Lead(tempEmail, new Date()));
			return "{\"exists\": false}";
		}else{
			return "{\"exists\": true}";
		}
		
	}
	
}
