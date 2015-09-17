package org.springframework.social.iservport.connect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.iservport.api.impl.RemoteUser;
import org.springframework.social.iservport.repository.RemoteUserRepository;
import org.springframework.social.iservport.utils.RemoteUserUtils;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Used to support the user signing in with one of their provider accounts.
 * 
 * @author mauriciofernandesdecastro
 */
public class RemoteUserSignInAdapter 
	implements SignInAdapter
{

	private final RemoteUserRepository remoteUserRepository;

	private final RequestCache requestCache;
	
	/**
	 * Constructor.
	 * 
	 * @param remoteUserRepository
	 * @param requestCache
	 */
	public RemoteUserSignInAdapter(RemoteUserRepository remoteUserRepository, RequestCache requestCache) {
		super();
		this.remoteUserRepository = remoteUserRepository;
		this.requestCache = requestCache;
	}

	@Override
	public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
		RemoteUser remoteUser = remoteUserRepository.findById(Integer.valueOf(userId));
		RemoteUserUtils.signin(remoteUser);
		return extractOriginalUrl(request);
	}

	// internal helpers
	
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
