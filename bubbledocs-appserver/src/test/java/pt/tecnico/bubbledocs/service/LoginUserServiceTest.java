package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

import org.junit.Test;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import pt.tecnico.bubbledocs.integration.LoginUserIntegrator;
import pt.tecnico.bubbledocs.BubbleDocsServiceTest;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

// add needed import declarations

public class LoginUserServiceTest extends BubbleDocsServiceTest {

	private String jp; // the token for user jp
	private String root; // the token for user root
	
	private static final String USERNAME = "sjp";
	private static final String PASSWORD = "sjp#";
	private static final String EMAIL = "sjp@ist.utl.pt";
	private static final String WRONG_USERNAME = "jp2";
	private static final String WRONG_PASSWORD = "jp2";

	@Override
	public void populate4Test() {
		
		createUser(USERNAME, "João Pereira", EMAIL);
	
	}

	// returns the time of the last access for the user with token userToken.
	// It must get this data from the session object of the application
	private DateTime getLastAccessTimeInSession(String token) {		
		BubbleDocs bd = BubbleDocs.getInstance();
		User u = bd.getUserByToken(token);
		return u.getSession().getLastAccess();
	}
	
	// eventual service tests will be added here

}