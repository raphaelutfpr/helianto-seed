package org.helianto.core.social;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Adaptador para sign-in.
 * 
 * @author mauriciofernandesdecastro
 */
public class SimpleSignInAdapter implements SignInAdapter {

	private static final Logger logger = LoggerFactory.getLogger(SimpleSignInAdapter.class);
	
	private final RequestCache requestCache; 
	
	private final UserSocialSignInService userSocialSignInService;
	
	/**
	 * Constructor.
	 * 
	 * @param requestCache
	 * @param entityRepository
	 * @param userInstallService
	 * @param authorizationChecker
	 */
	@Inject
	public SimpleSignInAdapter(RequestCache requestCache, UserSocialSignInService userSocialSignInService) {
		this.requestCache = requestCache;
		this.userSocialSignInService = userSocialSignInService;
	}
	
	@Override
	public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
		int userId = 0;
		try {
			userId = Integer.parseInt(localUserId);
			userSocialSignInService.signin(userId);
		}
		catch (Exception e) {
			logger.error("Unable to associate user details to id {}.", userId);
			e.printStackTrace();
		}
		return extractOriginalUrl(request);
	}

	private String extractOriginalUrl(NativeWebRequest request) {
		HttpServletRequest nativeReq = request.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse nativeRes = request.getNativeResponse(HttpServletResponse.class);
		SavedRequest saved = requestCache.getRequest(nativeReq, nativeRes);
		if (saved == null) {
			return null;
		}
		requestCache.removeRequest(nativeReq, nativeRes);
		removeAutheticationAttributes(nativeReq.getSession(false));
		return saved.getRedirectUrl();
	}
		 
	private void removeAutheticationAttributes(HttpSession session) {
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

}