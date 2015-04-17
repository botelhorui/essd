package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class CreateUserTest extends BubbleDocsServiceTest {

	// the tokens
	private String rootToken;
	private String arsToken;

	private static final String USERNAME = "ars";
	private static final String PASSWORD = "ars";
	private static final String ROOT_USERNAME = "root";
	private static final String USERNAME_DOES_NOT_EXIST = "no-one";
	private static final String USERNAME_DOES_NOT_EXIST2 = "no-one2";
	private static final String EMAIL = "no.one@yahoo.com";
	private static final String INVALID_EMAIL = "superinvalid.com";

	@Mocked
	private IDRemoteServices remoteService;

	
	@Override
	public void populate4Test() {

		createUser(PASSWORD,USERNAME,"António Rito Silva");
		rootToken = addUserToSession(ROOT_USERNAME);
		arsToken = addUserToSession(USERNAME);
	}

	
	@Test
	public void success() {
		
		CreateUser service = new CreateUser( rootToken, USERNAME_DOES_NOT_EXIST, "José Ferreira", EMAIL );

		// Expectations : Expected behaviour when class is called with specific arguments
		new Expectations() {{
			
			remoteService.createUser( USERNAME_DOES_NOT_EXIST, "José Ferreira", EMAIL );
		
		}};

		service.execute();		
		
		new Verifications() {{
			
			remoteService.createUser( USERNAME_DOES_NOT_EXIST, "José Ferreira", EMAIL ); times=1;
		
		}};

		// User is the domain class that represents a User
		User user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);

		assertEquals(USERNAME_DOES_NOT_EXIST, user.getUsername());
		assertEquals(EMAIL, user.getEmail());
		assertEquals("José Ferreira", user.getName());
	}

	
	@Test(expected = DuplicateUsernameException.class)
	public void usernameExists() {
		
		CreateUser service = new CreateUser(rootToken, USERNAME, "José Ferreira", EMAIL);
		
		new Expectations() {
			{
				remoteService.createUser( USERNAME , "José Ferreira", EMAIL );
				result = new DuplicateUsernameException();
			}
		};
		
		service.execute();
	
	}

	@Test(expected = EmptyUsernameException.class)
	public void emptyUsername() {
		
		CreateUser service = new CreateUser(rootToken, "", "José Ferreira", EMAIL);
		
		service.execute();
	
	}

	@Test(expected = UnauthorizedOperationException.class)
	public void unauthorizedUserCreation() {
		
		CreateUser service = new CreateUser(arsToken, USERNAME_DOES_NOT_EXIST, "José Ferreira", EMAIL);
		
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void accessUsernameNotExist() {
		
		removeUserFromSession(rootToken);
		
		CreateUser service = new CreateUser(rootToken, USERNAME_DOES_NOT_EXIST, "José Ferreira", EMAIL);
		
		service.execute();
	
	}
	
	@Test(expected = DuplicateEmailException.class)
	public void duplicateEmail() {
		
		CreateUser service = new CreateUser(rootToken, USERNAME_DOES_NOT_EXIST, "José Ferreira", EMAIL);
		CreateUser service2 = new CreateUser(rootToken, USERNAME_DOES_NOT_EXIST2, "José Ferreira", EMAIL);
		
		service.execute();
		service2.execute();
	
	}
	
	@Test(expected = InvalidEmailException.class)
	public void invalidEmail() {
		
		CreateUser service = new CreateUser(rootToken, USERNAME_DOES_NOT_EXIST, "José Ferreira", INVALID_EMAIL);
		
		service.execute();
	}
	

	@Test(expected = UnavailableServiceException.class)
	public void unavailableServiceException() {
		
		CreateUser service = new CreateUser(rootToken, USERNAME_DOES_NOT_EXIST, "José Ferreira", EMAIL);
		
		new Expectations() {
			{
				remoteService.createUser( USERNAME_DOES_NOT_EXIST , "José Ferreira", EMAIL );
				result = new RemoteInvocationException();
			}
		};
		
		service.execute();
		
		new Verifications() {{
			
			remoteService.createUser( USERNAME_DOES_NOT_EXIST, "José Ferreira", EMAIL ); times=1;
		
		}};
	}
	
	
}
