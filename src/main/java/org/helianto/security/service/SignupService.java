package org.helianto.security.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.helianto.core.domain.Identity;
import org.helianto.core.domain.Lead;
import org.helianto.core.domain.Signup;
import org.helianto.core.repository.IdentityRepository;
import org.helianto.core.repository.LeadRepository;
import org.helianto.core.repository.SignupRepository;
import org.helianto.core.sender.NotificationSender;
import org.helianto.user.domain.User;
import org.helianto.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

/**
 * Signup service.
 * 
 * @author mauriciofernandesdecastro
 */
@Service
public class SignupService {

	private static final Logger logger = LoggerFactory.getLogger(SignupService.class);
	
	private LeadRepository leadRepository;
	
	private IdentityRepository identityRepository;
	
	private SignupRepository signupRepository;
	
	private UserRepository userRepository;
	
	private NotificationSender notificationSender;
	
	private ProviderSignInUtils providerSignInUtils;
	
	/**
	 * Constructor.
	 * 
	 * @param leadRepository
	 * @param identityRepository
	 * @param signupRepository
	 * @param userRepository
	 * @param notificationSender
	 * @param connectionFactoryLocator
	 * @param connectionRepository
	 */
	@Inject
	public SignupService(LeadRepository leadRepository,
			IdentityRepository identityRepository,
			SignupRepository signupRepository, 
			UserRepository userRepository,
			NotificationSender notificationSender,
			ConnectionFactoryLocator connectionFactoryLocator,
			UsersConnectionRepository connectionRepository) {
		super();
		this.leadRepository = leadRepository;
		this.identityRepository = identityRepository;
		this.signupRepository = signupRepository;
		this.userRepository = userRepository;
		this.notificationSender = notificationSender;
		this.providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
	}
	
	/**
	 * Attempt to retrieve signup from social provider.
	 * 
	 * @param contextId
	 * @param request
	 */
	public Signup socialSignUpAttempt(int contextId, WebRequest request) {
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
		if (connection != null) {
			UserProfile providerUser = connection.fetchUserProfile();
			if (providerUser!=null) {
				return new Signup(contextId, providerUser.getEmail(), providerUser.getFirstName(), providerUser.getLastName());
			}
		}
		return new Signup(contextId, "");
	}
 
	/**
	 * Verify if email exists
	 * 
	 * @param signup
	 * */
	public boolean searchPrincipal(Signup signup){
		List<User> userList = userRepository.findByIdentityPrincipal(signup.getPrincipal());
		Identity identity = identityRepository.findByPrincipal(signup.getPrincipal());
		
		if(identity == null && userList.size()==0){ // se o email não exisite, pode inseri-lom entao retorna true
			return true;
		}
		return false;
	}
	
	/**
	 * True if all existing users for the identity are valid, otherwise admin is notified.
	 * 
	 * @param signup
	 */
	public boolean allUsersForIdentityAreValid(Signup signup) {
		// TODO consider contextId (inside signup already) to search for users (below).
		List<User> userList = userRepository.findByIdentityPrincipal(signup.getPrincipal());
		
		boolean isActive = userList!=null && userList.size()>0;
		if (!isActive) {
			return true;
		}
		
		for (User user: userList) {
			if (!user.isAccountNonLocked() || !user.isAccountNonExpired()) {
				notificationSender.send(signup.getPrincipal(), signup.getFirstName(), signup.getLastName(), "Inactive user attempting signup.");
				logger.warn("User {} inactive, notification sent", signup.getPrincipal());
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * True if all existing users for the identity are valid, otherwise admin is notified, except if principal is empty.
	 * 
	 * @param contextId
	 * @param principal
	 */
	public boolean notifyAdminIfUserIsNotValid(Integer contextId, String principal) {
		if (principal!=null && principal.length()>0) {
			Identity identity = identityRepository.findByPrincipal(principal);
			Signup signup = new Signup(contextId, principal);
			if (identity!=null) {
				signup.setFirstName(identity.getIdentityFirstName());
				
			}
			return allUsersForIdentityAreValid(signup);
		}
		return false;
	}
	
	/**
	 * Save a new lead or return true if existing.
	 * 
	 * @param principal
	 */
	public boolean saveLead(String principal) {
		Identity identity = identityRepository.findByPrincipal(principal);
		List<Lead> leads = leadRepository.findByPrincipal(principal);
		if(identity==null && (leads==null || leads.size()==0)){
			leadRepository.save(new Lead(principal, new Date()));
			return false;
		}
		return true;
	}

	/**
	 * Create and save a new lead.
	 * 
	 * @param id
	 * @param principal
	 */
	public Lead createLead(Integer id, String principal) {
		Lead lead = new Lead(id, principal, new Date());
		lead.setToken(createToken());
		return leadRepository.saveAndFlush(lead);
	}
	
	/**
	 * Create token.
	 */
	public String createToken() {
		UUID uuid = UUID.randomUUID();
		String token = uuid.toString();
		if (token.length()>36) {
    		token = token.substring(0, 36).trim();
		}
		return token;
	}
	
	/**
	 * Save the signup form.
	 * 
	 * @param signup
	 * @param ipAddress
	 */
	public Signup saveSignup(Signup signup, String ipAddress) {
		Identity identity = identityRepository.findByPrincipal(signup.getPrincipal());
		
		if (identity==null) {
			identity = identityRepository.saveAndFlush(signup.createIdentityFromForm());
			logger.info("New identity {} created", identity.getPrincipal());
		}
		// TODO save the ipAddress
		return signupRepository.saveAndFlush(signup);
	}
	
}
