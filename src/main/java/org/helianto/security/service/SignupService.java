package org.helianto.security.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
import org.springframework.social.connect.UserProfile;
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
	 */
	public SignupService(LeadRepository leadRepository,
			IdentityRepository identityRepository,
			SignupRepository signupRepository, UserRepository userRepository,
			NotificationSender notificationSender) {
		super();
		this.leadRepository = leadRepository;
		this.identityRepository = identityRepository;
		this.signupRepository = signupRepository;
		this.userRepository = userRepository;
		this.notificationSender = notificationSender;
		this.providerSignInUtils = new ProviderSignInUtils();
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
	 * True if all existing users for the identity are valid, otherwise admin is notified.
	 * 
	 * @param signup
	 */
	public boolean notifyAdminIfUserIsNotValid(Signup signup) {
		List<User> userList = userRepository.findByIdentityPrincipal(signup.getPrincipal());
		
		boolean isActive = userList!=null && userList.size()>0;
		if (!isActive) {
			logger.warn("Empty user list for {}.", signup.getPrincipal());
			notificationSender.send(signup.getPrincipal(), signup.getFirstName(), signup.getLastName(), "Empty user list for identity attempting signup.");
			return false;
		}
		
		for (User user: userList) {
			if (!user.isAccountNonLocked() || !user.isAccountNonExpired()) {
				isActive = false;
				break;
			}
		}
		
		if (isActive) {
			return true;
		}
		
		notificationSender.send(signup.getPrincipal(), signup.getFirstName(), signup.getLastName(), "Inactive user attempting signup.");
		logger.warn("User {} inactive, notification sent", signup.getPrincipal());
		return false;
	}
	
	/**
	 * True if all existing users for the identity are valid, otherwise admin is notified, except if principal is empty.
	 * 
	 * @param principal
	 */
	public boolean notifyAdminIfUserIsNotValid(String principal) {
		if (principal!=null && principal.length()>0) {
			Identity identity = identityRepository.findByPrincipal(principal);
			Signup signup = new Signup(1, principal);
			if (identity!=null) {
				signup.setFirstName(identity.getIdentityFirstName());
				
			}
			return notifyAdminIfUserIsNotValid(signup);
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
		UUID uuid = UUID.randomUUID();
		String token = uuid.toString();
		if (token.length()>36) {
    		token = token.substring(0, 36).trim();
		}
		lead.setToken(token);
		return leadRepository.saveAndFlush(lead);
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
