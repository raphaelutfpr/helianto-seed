package org.helianto.security.controller;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.helianto.seed.TestConfig;
import org.helianto.seed.TestConfig.VerifyControllerStub;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author mauriciofernandesdecastro
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestConfig.class})
@ActiveProfiles("standalone")
@Transactional
public class VerifyControllerTests {
	
	@Inject
	private VerifyControllerStub verifyControllerStub;

	@Test
	public void test() {
		assertNotNull(verifyControllerStub);
	}

}
