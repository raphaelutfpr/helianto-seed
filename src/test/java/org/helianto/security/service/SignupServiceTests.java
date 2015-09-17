package org.helianto.security.service;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.easymock.EasyMock;
import org.helianto.core.domain.Identity;
import org.helianto.core.domain.Lead;
import org.helianto.core.domain.Signup;
import org.helianto.core.repository.IdentityRepository;
import org.helianto.core.repository.LeadRepository;
import org.helianto.core.repository.SignupRepository;
import org.helianto.core.sender.NotificationSender;
import org.helianto.user.domain.User;
import org.helianto.user.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author mauriciofernandesdecastro
 */
public class SignupServiceTests {

	@Test
	public void notifyAdminIfUserIsNotValid1() {
		Signup signup = new Signup();
		signup.setPrincipal("principal");
		
		// create user list, all valid
		List<User> userList = new ArrayList<>();
		User user1 = new User();
		user1.setAccountNonExpired(true);
		assertTrue(user1.isAccountNonLocked());
		userList.add(user1);
		User user2 = new User();
		user2.setAccountNonExpired(true);
		assertTrue(user2.isAccountNonLocked());
		userList.add(user2);
		expect(userRepository.findByIdentityPrincipal("principal")).andReturn(userList);
		replay(userRepository);
		
		assertTrue(signupService.allUsersForIdentityAreValid(signup));
		
	}

	@Test
	public void notifyAdminIfUserIsNotValid2() {
		Signup signup = new Signup();
		signup.setPrincipal("principal");
		
		// empty user list
		List<User> userList = new ArrayList<>();
		expect(userRepository.findByIdentityPrincipal("principal")).andReturn(userList);
		replay(userRepository);
		
		assertTrue(signupService.allUsersForIdentityAreValid(signup));
		
	}

	@Test
	public void notifyAdminIfUserIsNotValid3() {
		Signup signup = new Signup();
		signup.setPrincipal("principal");
		
		// null user list
		List<User> userList = null;
		expect(userRepository.findByIdentityPrincipal("principal")).andReturn(userList);
		replay(userRepository);
		
		assertTrue(signupService.allUsersForIdentityAreValid(signup));
		
	}

	@Test
	public void notifyAdminIfUserIsNotValid4() {
		Signup signup = new Signup();
		signup.setPrincipal("principal");
		
		// create user list, one invalid
		List<User> userList = new ArrayList<>();
		User user1 = new User();
		user1.setAccountNonExpired(true);
		assertTrue(user1.isAccountNonLocked());
		userList.add(user1);
		User user2 = new User();
		user2.setAccountNonExpired(false);
		assertTrue(user2.isAccountNonLocked());
		userList.add(user2);
		expect(userRepository.findByIdentityPrincipal("principal")).andReturn(userList);
		replay(userRepository);
		
		assertFalse(signupService.allUsersForIdentityAreValid(signup));
		
	}

	@Test
	public void notifyAdminIfUserIsNotValid5() {
		
		// the identity
		Identity identity = new Identity();
		identity.setId(1);
		expect(identityRepository.findByPrincipal("principal")).andReturn(identity);
		replay(identityRepository);
		
		// any user list, can be null
		List<User> userList = null;
		expect(userRepository.findByIdentityPrincipal("principal")).andReturn(userList);
		replay(userRepository);
		
		signupService.notifyAdminIfUserIsNotValid(1, "principal");
		assertTrue(calledNotifyAdminIfUserIsNotValid);
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

	@Test
	public void saveLead1() {
		
		expect(identityRepository.findByPrincipal("principal")).andReturn(null);
		replay(identityRepository);
		
		List<Lead> leads = null;
		expect(leadRepository.findByPrincipal("principal")).andReturn(leads);
		expect(leadRepository.save(EasyMock.isA(Lead.class))).andReturn(null);
		replay(leadRepository);
		
		assertFalse(signupService.saveLead("principal"));
	}
	
	@Test
	public void saveLead2() {
		
		expect(identityRepository.findByPrincipal("principal")).andReturn(null);
		replay(identityRepository);
		
		List<Lead> leads = new ArrayList<>();
		expect(leadRepository.findByPrincipal("principal")).andReturn(leads);
		expect(leadRepository.save(EasyMock.isA(Lead.class))).andReturn(null);
		replay(leadRepository);
		
		assertFalse(signupService.saveLead("principal"));
	}
	
	@Test
	public void saveLead3() {
		
		Identity identity = new Identity();
		identity.setId(1);
		expect(identityRepository.findByPrincipal("principal")).andReturn(identity);
		replay(identityRepository);
		
		List<Lead> leads = new ArrayList<>();
		leads.add(new Lead());
		expect(leadRepository.findByPrincipal("principal")).andReturn(leads);
		replay(leadRepository);
		
		assertTrue(signupService.saveLead("principal"));
	}
	
	private SignupService signupService;
	private LeadRepository leadRepository;
	private IdentityRepository identityRepository;
	private SignupRepository signupRepository;
	private UserRepository userRepository;
	private NotificationSender notificationSender;
	private Boolean calledNotifyAdminIfUserIsNotValid = false;
	
	@Before
	public void setup() {
		leadRepository = EasyMock.createMock(LeadRepository.class);
		identityRepository = EasyMock.createMock(IdentityRepository.class);
		signupRepository = EasyMock.createMock(SignupRepository.class);
		userRepository = EasyMock.createMock(UserRepository.class);
		notificationSender = new NotificationSender() {
			@Override 
			public boolean send(String recipientEmail, String recipientFirstName, String recipientLastName
					, String subject, String... params) {
				return true;
			}
		};
		signupService = new SignupService(leadRepository, identityRepository, signupRepository, userRepository, notificationSender, null, null) {
			@Override
			public boolean allUsersForIdentityAreValid(Signup signup) {
				calledNotifyAdminIfUserIsNotValid = true;
				return super.allUsersForIdentityAreValid(signup);
			}
		};
	}
	
	@After
	public void tearDown() {
		EasyMock.reset(leadRepository);
		EasyMock.reset(identityRepository);
		EasyMock.reset(signupRepository);
		EasyMock.reset(userRepository);
		calledNotifyAdminIfUserIsNotValid = false;
	}
	
}
