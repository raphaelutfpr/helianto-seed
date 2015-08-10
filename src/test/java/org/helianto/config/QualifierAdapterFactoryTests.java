package org.helianto.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.helianto.config.NetworkQualifierStub.InternalNetworkStub;
import org.helianto.config.UserQualifierStub.InternalUserStub;
import org.helianto.core.config.HeliantoConfig;
import org.helianto.core.internal.QualifierAdapter;
import org.helianto.qualifier.QualifierAdapterList;
import org.helianto.qualifier.AbstractQualifierAdapterList.NetworkKeyNameAdapter;
import org.helianto.qualifier.AbstractQualifierAdapterList.UserKeyNameAdapter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={HeliantoConfig.class, TestConfig.class})
public class QualifierAdapterFactoryTests {
	
	@Inject @NetworkKeyNameAdapter QualifierAdapterList networkList;

	@Inject @UserKeyNameAdapter QualifierAdapterList userList;

	@Test
	public void test() throws Exception {
		assertNotNull(networkList);
		assertTrue(networkList.getQualifierList().contains(new QualifierAdapter(InternalNetworkStub.ONE)));
		assertNotNull(userList);
		assertTrue(userList.getQualifierList().contains(new QualifierAdapter(InternalUserStub.BETA)));
	}

}
