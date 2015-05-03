package pt.tecnico.bubbledocs.service;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

public class GetUsername4TokenServiceTest extends BubbleDocsServiceTest{
	//tokens
	private String sun; // the token for user sun
	
	// the perfect user
	private static final String USERNAME = "sun";
	private static final String NAME = "sun#";
	private static final String EMAIL = "sun@sun.com";
			
	@Override
	public void populate4Test() {
		createUser(USERNAME, NAME, EMAIL);
		
		sun = addUserToSession(USERNAME);
	}
	
	@Test
	public void success() {	
		BubbleDocs bd = BubbleDocs.getInstance();
    	
    	GetUsername4TokenService service = new GetUsername4TokenService(sun);
		service.execute();
		
		String user = service.getUsername();
		
		assertEquals("Service is returning a different username.", user, USERNAME);
	}
	
	@Test
	public void success2() {
		GetUsername4TokenService service = new GetUsername4TokenService("");
		service.execute();
		String user = service.getUsername();
		
		assertNull("If there the token is empty the username returned should be null.", user);
	}
	
}