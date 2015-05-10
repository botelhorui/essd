package pt.tecnico.bubbledocs.service;

import org.junit.Assert;
import org.junit.Test;

import pt.tecnico.bubbledocs.BubbleDocsServiceTest;

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
	
	// the not in session user
	private static final String USERNAME1 = "moon";
	private static final String NAME1 = "moon#";
	private static final String EMAIL1 = "moon@moon.com";
	
	
			
	@Override
	public void populate4Test() {
		createUser(USERNAME, NAME, EMAIL);
		createUser(USERNAME1, NAME1, EMAIL1);
		
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
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		GetUsername4TokenService service = new GetUsername4TokenService("moon");
		service.execute();
	}
	
}