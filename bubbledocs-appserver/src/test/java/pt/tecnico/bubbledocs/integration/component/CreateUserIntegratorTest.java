package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;

import pt.tecnico.bubbledocs.integration.CreateUserIntegrator;

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
import pt.tecnico.bubbledocs.exception.CharacterLimitException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class CreateUserIntegratorTest extends BubbleDocsIntegratorTest {

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
	private static final String INVALID_USERNAME = "supercalifragilisticexpialidocious";
	private static final String REMOTE_INVALID_USERNAME = "NoLikeU";

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
		
		CreateUserIntegrator service = new CreateUserIntegrator( rootToken, USERNAME_DOES_NOT_EXIST, "José Ferreira", EMAIL );

		// Expectations : Expected behaviour when class is called with specific arguments
		new Expectations() {{
			
			remoteService.createUser( USERNAME_DOES_NOT_EXIST, "José Ferreira", EMAIL );
		
		}};

		service.execute();		

		// User is the domain class that represents a User
		User user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);

		assertEquals(USERNAME_DOES_NOT_EXIST, user.getUsername());
		assertEquals(EMAIL, user.getEmail());
		assertEquals("José Ferreira", user.getName());
	}

	
	@Test(expected = DuplicateUsernameException.class)
	public void usernameExists() {
		
		CreateUserIntegrator service = new CreateUserIntegrator(rootToken, USERNAME, "José Ferreira", EMAIL);
		
		new Expectations() {
			{
				remoteService.createUser( USERNAME , "José Ferreira", EMAIL );
				result = new DuplicateUsernameException();
			}
		};
		
		service.execute();
	
	}

	@Test(expected = InvalidUsernameException.class)
	public void InvalidUsername() {
		
		CreateUserIntegrator service = new CreateUserIntegrator(rootToken, REMOTE_INVALID_USERNAME, "José Ferreira", EMAIL);
		
		new Expectations() {
			{
				remoteService.createUser( REMOTE_INVALID_USERNAME , "José Ferreira", EMAIL );
				result = new InvalidUsernameException();
			}
		};
		
		service.execute();
	
	}
	
	@Test(expected = CharacterLimitException.class)
	public void characterLimit() {
		
		CreateUserIntegrator service = new CreateUserIntegrator(rootToken, INVALID_USERNAME, "Mary Poppins", EMAIL);
		
		service.execute();
	
	}

	@Test(expected = UnauthorizedOperationException.class)
	public void unauthorizedUserCreation() {
		
		CreateUserIntegrator service = new CreateUserIntegrator(arsToken, USERNAME_DOES_NOT_EXIST, "José Ferreira", EMAIL);
		
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void accessUsernameNotExist() {
		
		removeUserFromSession(rootToken);
		
		CreateUserIntegrator service = new CreateUserIntegrator(rootToken, USERNAME_DOES_NOT_EXIST, "José Ferreira", EMAIL);
		
		service.execute();
	
	}
	
	@Test(expected = DuplicateEmailException.class)
	public void duplicateEmail() {
		
		CreateUserIntegrator service = new CreateUserIntegrator(rootToken, USERNAME_DOES_NOT_EXIST, "José Ferreira", EMAIL);
		CreateUserIntegrator service2 = new CreateUserIntegrator(rootToken, USERNAME_DOES_NOT_EXIST2, "José Ferreira", EMAIL);
		
		service.execute();
		
		new Expectations() {
			{
				remoteService.createUser( USERNAME_DOES_NOT_EXIST2 , "José Ferreira", EMAIL );
				result = new DuplicateEmailException();
			}
		};
		
		service2.execute();
	
	}
	
	@Test(expected = InvalidEmailException.class)
	public void invalidEmail() {
		
		CreateUserIntegrator service = new CreateUserIntegrator(rootToken, USERNAME_DOES_NOT_EXIST, "José Ferreira", INVALID_EMAIL);
		
		new Expectations() {
			{
				remoteService.createUser( USERNAME_DOES_NOT_EXIST , "José Ferreira", INVALID_EMAIL );
				result = new InvalidEmailException();
			}
		};
		
		service.execute();
	}
	

	@Test(expected = UnavailableServiceException.class)
	public void unavailableServiceException() {
		
		CreateUserIntegrator service = new CreateUserIntegrator(rootToken, USERNAME_DOES_NOT_EXIST, "José Ferreira", EMAIL);
		
		new Expectations() {
			{
				remoteService.createUser( USERNAME_DOES_NOT_EXIST , "José Ferreira", EMAIL );
				result = new RemoteInvocationException();
			}
		};
		
		service.execute();
		
	}
	
	
}
