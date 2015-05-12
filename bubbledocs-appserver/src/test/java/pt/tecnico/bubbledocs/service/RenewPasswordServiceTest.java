package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

import org.joda.time.DateTime;

import pt.tecnico.bubbledocs.integration.RenewPasswordIntegrator;
import pt.tecnico.bubbledocs.BubbleDocsServiceTest;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;

public class RenewPasswordServiceTest extends BubbleDocsServiceTest{
	private String moonToken;
	private static final String USERNAME = "moon";
	private static final String EMAIL = "diamond@pinkfloyd.com";
	private static final String NAME = "Shine";


	@Mocked	private IDRemoteServices IDRemoteService;

	//Setup
	public void populate4Test(){
		//Create user
		createUser(USERNAME, NAME, EMAIL);

		//Log in user
		moonToken = addUserToSession(USERNAME);
		
	}

	// eventual service tests will be added here
	
	
}