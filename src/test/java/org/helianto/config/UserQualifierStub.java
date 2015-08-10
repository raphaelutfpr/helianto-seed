package org.helianto.config;

import java.io.Serializable;

import org.helianto.core.internal.KeyNameAdapter;
import org.helianto.qualifier.AbstractQualifierAdapterList;
import org.helianto.qualifier.AbstractQualifierAdapterList.UserKeyNameAdapter;

/**
 * Test stub.
 * 
 * @author mauriciofernandesdecastro
 */
@UserKeyNameAdapter
public class UserQualifierStub 
	extends AbstractQualifierAdapterList
{

	@Override
	protected KeyNameAdapter[] getQualifierArray() {
		return InternalUserStub.values();
	}
	
	public enum InternalUserStub implements KeyNameAdapter {
		
		ALPHA, BETA;

		@Override
		public String getCode() {
			return name();
		}

		@Override
		public Serializable getKey() {
			return name();
		}

		@Override
		public String getName() {
			return name();
		}
		
	}

}
