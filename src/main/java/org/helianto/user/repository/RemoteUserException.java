package org.helianto.user.repository;

/**
 * Base class of the RemoteUserException hierarchy.
 * 
 * Marked abstract, as its designed to be subclasses.
 * A checked Exception, as AccountExceptions are recoverable business exceptions.
 * 
 * @author mauriciofernandesdecastro
 */
@SuppressWarnings("serial")
public abstract class RemoteUserException extends Exception {

	public RemoteUserException(String message) {
		super(message);
	}

	public RemoteUserException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
