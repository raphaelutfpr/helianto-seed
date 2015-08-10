package org.helianto.config;

import java.io.Serializable;

import org.helianto.core.internal.KeyNameAdapter;
import org.helianto.qualifier.AbstractQualifierAdapterList;
import org.helianto.qualifier.AbstractQualifierAdapterList.NetworkKeyNameAdapter;

/**
 * Test stub.
 * 
 * @author mauriciofernandesdecastro
 */
@NetworkKeyNameAdapter
public class NetworkQualifierStub 
	extends AbstractQualifierAdapterList
{

	@Override
	protected KeyNameAdapter[] getQualifierArray() {
		return InternalNetworkStub.values();
	}
	
	public enum InternalNetworkStub implements KeyNameAdapter {
		
		ONE, TWO;

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
