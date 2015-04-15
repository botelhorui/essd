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

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UnknownBubbleDocsUserException;
import pt.tecnico.bubbledocs.exception.WrongPasswordException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

// add needed import declarations

public class LoginUserTest extends BubbleDocsServiceTest {

	private String jp; // the token for user jp
	private String root; // the token for user root
	@Mocked
	private IDRemoteServices remoteService;

	private static final String USERNAME = "jp";
	private static final String PASSWORD = "jp#";
	private static final String WRONG_USERNAME = "jp2";
	private static final String WRONG_PASSWORD = "jp2";

	@Override
	public void populate4Test() {
		createUser(USERNAME, null, "João Pereira");
	}

	// returns the time of the last access for the user with token userToken.
	// It must get this data from the session object of the application
	private DateTime getLastAccessTimeInSession(String token) {		
		BubbleDocs bd = BubbleDocs.getInstance();
		User u = bd.getUserByToken(token);
		return u.getSession().getLastAccess();
	}

	@Test
	public void success() {
		LoginUser service = new LoginUser(USERNAME, PASSWORD);
		new Expectations() {{
			remoteService.loginUser(USERNAME, PASSWORD);
		}};
		service.execute();
		new Verifications() {{
			remoteService.loginUser(USERNAME, PASSWORD); times=1;
		}};

		DateTime currentTime = new DateTime();
		String token = service.getUserToken();
		User user = getUserFromSession(service.getUserToken());
		assertEquals(USERNAME, user.getUsername());

		int difference = Seconds.secondsBetween(getLastAccessTimeInSession(token), currentTime).getSeconds();

		assertTrue("Access time in session not correctly set", difference >= 0);
		assertTrue("diference in seconds greater than expected", difference < 2);
	}

	@Test
	public void successLoginTwice() {
		LoginUser service = new LoginUser(USERNAME, PASSWORD);
		new Expectations() {{
			remoteService.loginUser(USERNAME, PASSWORD);
		}};
		service.execute();
		new Verifications() {{
			remoteService.loginUser(USERNAME, PASSWORD); times=1;
		}};

		String token1 = service.getUserToken();

		new Expectations() {{
			remoteService.loginUser(USERNAME, PASSWORD);
		}};
		service.execute();
		new Verifications() {{
			remoteService.loginUser(USERNAME, PASSWORD); times=1;
		}};

		String token2 = service.getUserToken();

		User user = getUserFromSession(token1);
		assertNull(user);
		user = getUserFromSession(token2);
		assertEquals(USERNAME, user.getUsername());
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void loginUserWithLocalCopyWithWrongPassword() {
		LoginUser service = new LoginUser(USERNAME, PASSWORD);
		new Expectations() {{
			remoteService.loginUser(USERNAME, PASSWORD);
		}};		
		service.execute();
		// local copy is set
		service = new LoginUser(USERNAME, WRONG_PASSWORD);		
		new Expectations() {{
			remoteService.loginUser(USERNAME, WRONG_PASSWORD);
			result = new LoginBubbleDocsException();
		}};		
		service.execute();
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void loginUserWithoutLocalCopyWithWrongPassword() {
		LoginUser service = new LoginUser(USERNAME, WRONG_PASSWORD);

		new Expectations() {{
			remoteService.loginUser(USERNAME, WRONG_PASSWORD);
			result = new LoginBubbleDocsException();
		}};

		service.execute();
	}

	@Test
	public void loginRemoteServiceDownWithLocalCopy() {
		LoginUser service = new LoginUser(USERNAME, PASSWORD);
		new Expectations() {{
			remoteService.loginUser(USERNAME, PASSWORD);
		}};
		service.execute();
		new Verifications() {{
			remoteService.loginUser(USERNAME, PASSWORD); times=1;
		}};

		//local copy created		
		new Expectations() {{
			remoteService.loginUser(USERNAME, PASSWORD);
			result = new RemoteInvocationException();
		}};		
		service.execute();
		User user = getUserFromSession(service.getUserToken());
		assertEquals(USERNAME, user.getUsername());
	}

	@Test(expected = UnavailableServiceException.class)
	public void loginRemoteServiceDownWithLocalCopyWrongPassword() {
		LoginUser service = new LoginUser(USERNAME, PASSWORD);
		new Expectations() {{
			remoteService.loginUser(USERNAME, PASSWORD);
			remoteService.loginUser(USERNAME, PASSWORD);
			result = new RemoteInvocationException();
		}};
		service.execute();

		//local copy created		

		service = new LoginUser(USERNAME, WRONG_PASSWORD);
		service.execute();		
	}

	@Test(expected = UnavailableServiceException.class)
	public void loginRemoteServiceDownWithoutLocalCopy(){
		LoginUser service = new LoginUser(USERNAME, PASSWORD);
		new Expectations() {{
			remoteService.loginUser(USERNAME, PASSWORD);
			result = new RemoteInvocationException();
		}};
		service.execute();	
	}


	@Test(expected = LoginBubbleDocsException.class)
	public void loginUnknownUser() {
		LoginUser service = new LoginUser(WRONG_USERNAME, PASSWORD);
		service.execute();
	}


}