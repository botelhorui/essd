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

public class RenewPasswordIntegratorTest extends BubbleDocsServiceTest{
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

	@Test
	public void success(){
		/* Success case:
		 * - Valid user (exists and is in session)
		 * - Connects to remote service and renews password with IDRemoteServices.renewPassword()
		 * - Invalidates local copy of password
		 * - Session time updated
		 */
		BubbleDocs bd = BubbleDocs.getInstance();
		RenewPasswordIntegrator rp = new RenewPasswordIntegrator(moonToken);
		User moon = bd.getUserByToken(moonToken);
		new Expectations() {
			{
				IDRemoteService.renewPassword(USERNAME);
			}
		};
		DateTime start = moon.getSession().getLastAccess();	
		rp.execute();
		new Verifications() {
			{
				IDRemoteService.renewPassword(USERNAME); times=1;
			}
		};
		DateTime end = moon.getSession().getLastAccess();

		//TESTS
		assertNull("Error: Local copy of password not invalidated.", moon.getPassword());
		assertFalse("Error: Session time not updated.", end == start);

	}

	@Test(expected = LoginBubbleDocsException.class)
	public void userDoesNotExistError(){
		RenewPasswordIntegrator rp = new RenewPasswordIntegrator(moonToken);
		new Expectations() {
			{
				IDRemoteService.renewPassword(USERNAME);
				result = new LoginBubbleDocsException();
			}
		};
		rp.execute();
	}

	@Test(expected = UnavailableServiceException.class)
	public void UnavailableServiceError(){
		RenewPasswordIntegrator rp = new RenewPasswordIntegrator(moonToken);
		new Expectations() {
			{
				IDRemoteService.renewPassword(USERNAME);
				result = new RemoteInvocationException();
			}
		};
		rp.execute();
	}

}