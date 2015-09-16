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
package org.helianto.user.repository;


/**
 * The provided signin name could not be mapped.
 * 
 * @author mauriciofernandesdecastro
 */
@SuppressWarnings("serial")
public final class UserKeyNotFoundException extends RemoteUserException {

	private final String username;
	
	public UserKeyNotFoundException(String principal) {
		super("Principal not found");
		this.username = principal;
	}
	
	public String getUsername() {
		return username;
	}

}
