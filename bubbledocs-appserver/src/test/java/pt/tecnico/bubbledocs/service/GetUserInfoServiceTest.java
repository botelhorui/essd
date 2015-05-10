package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertTrue;
import org.junit.Assert;
import org.junit.Test;

import pt.tecnico.bubbledocs.BubbleDocsServiceTest;

import pt.tecnico.bubbledocs.service.dto.UserDTO;

public class GetUserInfoServiceTest extends BubbleDocsServiceTest{
	private static final String USERNAME = "rui";
	private static final String NAME = "Rui";
	private static final String EMAIL = "rui@rui.rui";
	
	@Override
	public void populate4Test() {
		createUser(USERNAME, NAME, EMAIL);
	}
	
	@Test
	public void success() {	
		GetUserInfoService service = new GetUserInfoService(USERNAME);
		service.execute();
		UserDTO user = service.getResult();
		
		assertTrue("different username", user.getUsername().equals(USERNAME));
		assertTrue("different name", user.getName().equals(NAME));
		assertTrue("different email", user.getEmail().equals(EMAIL));
		assertTrue("password should be null", user.getPassword() == null);
	}
	
	@Test
	public void success2() {	
		GetUserInfoService service = new GetUserInfoService("?");
		service.execute();
		UserDTO user = service.getResult();		
		
		assertTrue("not found user should be null", user == null);
	}
}
