package org.springframework.social.iservport.connect;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.iservport.api.Iservport;
import org.springframework.social.iservport.api.impl.RemoteUser;
import org.springframework.social.iservport.repository.RemoteUserRepository;
import org.springframework.social.iservport.repository.UserKeyAlreadyOnFileException;
import org.springframework.social.iservport.utils.RemoteUserUtils;

/**
 * Remote user connection sign-up
 * 
 * @author mauriciofernandesdecastro
 */
public class RemoteUserConnectionSignUp implements ConnectionSignUp {

    private final RemoteUserRepository remoteUserRepository;

    public RemoteUserConnectionSignUp(RemoteUserRepository remoteUserRepository) {
        this.remoteUserRepository = remoteUserRepository;
    }

    public String execute(Connection<?> connection) {
    	Iservport iservport = (Iservport) connection.getApi();
    	RemoteUser remoteUser = iservport.getProfile();
        try {
			remoteUserRepository.createRemoteUser(remoteUser);
			RemoteUserUtils.signin(remoteUser);
		} catch (UserKeyAlreadyOnFileException e) {
			e.printStackTrace();
		}
        return remoteUser.getUserId();
    }
	
}