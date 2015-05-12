package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.bubbledocs.integration.DeleteUserIntegrator;
import pt.tecnico.bubbledocs.BubbleDocsServiceTest;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

// add needed import declarations

public class DeleteUserIntegratorTest extends BubbleDocsServiceTest {

    private static final String USERNAME_TO_DELETE = "smf";
    private static final String USERNAME = "ars";
    private static final String EMAIL = "ars@ist.utl.pt";
    private static final String ROOT_USERNAME = "root";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";
    private static final String SPREADSHEET_NAME = "spread";

    // the tokens for user root
    private String root;
    
    @Mocked
	private IDRemoteServices remoteService;

    @Override
    public void populate4Test() {
        createUser(USERNAME, "António Rito Silva", EMAIL);
        User smf = createUser(USERNAME_TO_DELETE, "Sérgio Fernandes", "smf@ist.utl.pt");
        createSpreadSheet(smf, USERNAME_TO_DELETE, 20, 20);

        root = addUserToSession(ROOT_USERNAME);
    };
    
    @Test
    public void rollbackTest(){
    	DeleteUserIntegrator dui = new DeleteUserIntegrator(root, USERNAME_TO_DELETE);
    	User user = getUserFromUsername(USERNAME_TO_DELETE);
    	String originalUsername = user.getUsername();
    	String originalName = user.getName();
    	String originalEmail = user.getEmail();
    	
    	new Expectations(){{
			remoteService.removeUser(USERNAME_TO_DELETE); 
			result = new RemoteInvocationException();
		}};
		
		try{
			dui.execute();
		} catch (UnavailableServiceException e) {
			User deletedUser = getUserFromUsername(USERNAME_TO_DELETE);
			assertNotNull("Error: Local user was not recreated.", deletedUser);
			assertEquals("Error: Usernames do not match.", originalUsername, deletedUser.getUsername());
			assertEquals("Error: Names do not match.", originalName, deletedUser.getName());
			assertEquals("Error: Emails do not match.", originalEmail, deletedUser.getEmail());
			return;
		}
		
		fail("Error: Service did not throw expected exception.");
    }
    
}
