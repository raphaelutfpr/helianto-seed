package org.helianto.network.service;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.helianto.core.domain.Entity;
import org.helianto.security.internal.UserAuthentication;
import org.helianto.security.repository.UserAuthorityReadAdapter;
import org.helianto.security.repository.UserAuthorityRepository;
import org.helianto.user.domain.User;
import org.helianto.user.repository.UserGroupRepository;
import org.helianto.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * Entity network command service.
 * 
 * @author mauriciofernandesdecastro
 */
@Service
public class RootCommandService {
	
	private static final Logger logger = LoggerFactory.getLogger(RootCommandService.class);

	@Inject
	private UserRepository userRepository;
	
	@Inject
	private UserGroupRepository userGroupRepository;
	
	@Inject 
	private UserAuthorityRepository userAuthorityRepository;
	
//	/**
//	 * Item found.
//	 * 
//	 * @param model
//	 * @param terget
//	 * @deprecated
//	 */
//	public String view(UserAuthentication userAuthentication, Model model, Entity entityTarget, User target) {
//		logger.debug("Found user id {} for entity {}.", target.getId(), entityTarget);
//		if (target!=null) {
//			
//			model.addAttribute("root", target);
//			model.addAttribute("rootEntity", entityTarget);
//			model.addAttribute("qualifier", entityTarget.getEntityType());
//			
//		}
//		return "frame";
//	}
//	
	/**
	 * New authorization.
	 * 
	 * @param userId
	 * @param currentUserId
	 */
	public boolean authorize(Integer userId, Integer currentUserId) {
		User user = userRepository.findOne(userId);
		if (user!=null) {
			logger.debug("User to authorize as root is {}.", user);
			if (user.getIdentity()!=null && user.getIdentity().getId()!=0) {
				User current = userRepository.findOne(currentUserId);
				if (current!=null) {
					logger.debug("Current user is {}.", current);
					if (current.getIdentity()!=null) {
						if (current.getIdentity().getId()==user.getIdentity().getId()) {
							// TODO pode autorizar? ativo?
							logger.debug("Ready to authorize user {}.", user);
							user.setLastEvent(new Date());
//							Set<UserRole> roles = securityMgr.findRoles(user, true);
//							UserDetailsAdapter userDetails = new UserDetailsAdapter(user, null, roles);
//							Authentication authentication = new UsernamePasswordAuthenticationToken(
//									userDetails, "any", userDetails.getAuthorities()); // PreAuthenticatedAuthenticationToken(aPrincipal,
//																						// aCredentials,
//																						// anAuthorities);
							
							List<UserAuthorityReadAdapter> roles = userAuthorityRepository
									.findByUserGroupIdOrderByServiceCodeAsc(userGroupRepository.findParentsByChildId(user.getId()));
							User newUser = userRepository.saveAndFlush(user);
							SecurityContextHolder.clearContext();
							logger.debug("Authorized user {}, needs login...", newUser.getId());
							return true;
						}
						else {
							logger.debug("Unauthorized, current identity {} does "
									+ "not match new root user identity {}."
									, current.getIdentity().getId(), user.getIdentity().getId());
						}
					}
					else {
						logger.debug("Unauthorized, current identity is null.");
					}
				}
				else {
					logger.debug("Unauthorized, current user was not retrieved.");
				}
			}
			else {
				logger.debug("Unauthorized, invalid identity to swittch to, {}.", user.getIdentity());
			}
		}
		else {
			logger.debug("Unauthorized, root user to switch to is null.");
		}
		return false;
	}
	
}
