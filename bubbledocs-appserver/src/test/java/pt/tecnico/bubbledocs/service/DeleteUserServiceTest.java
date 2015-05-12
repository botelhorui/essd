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

public class DeleteUserServiceTest extends BubbleDocsServiceTest {

    private static final String USERNAME_TO_DELETE = "smf";
    private static final String USERNAME = "ars";
    private static final String EMAIL = "ars@ist.utl.pt";
    private static final String ROOT_USERNAME = "root";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";
    private static final String SPREADSHEET_NAME = "spread";

    // the tokens for user root
    private String root;

    @Override
    public void populate4Test() {
        createUser(USERNAME, "António Rito Silva", EMAIL);
        User smf = createUser(USERNAME_TO_DELETE, "Sérgio Fernandes", "smf@ist.utl.pt");
        createSpreadSheet(smf, USERNAME_TO_DELETE, 20, 20);

        root = addUserToSession(ROOT_USERNAME);
    };

    public void success() {
    	
    	User user = getUserFromUsername(USERNAME_TO_DELETE);    	
        DeleteUserIntegrator service = new DeleteUserIntegrator(root, USERNAME_TO_DELETE);
        service.execute();
        User deleted = getUserFromUsername(USERNAME_TO_DELETE);   	
        
        assertNull("user was not deleted", deleted);
        assertNull("Spreadsheet was not deleted", getSpreadSheet(SPREADSHEET_NAME));
    }

    /*
     * accessUsername exists, is in session and is root toDeleteUsername exists
     * and is not in session
     */
    @Test
    public void successToDeleteIsNotInSession() {
        success();
    }

    
    /*
     * accessUsername exists, is in session and is root toDeleteUsername exists
     * and is in session Test if user and session are both deleted
     */
    @Test
    public void successToDeleteIsInSession() {
        String token = addUserToSession(USERNAME_TO_DELETE);
        success();
	assertNull("Removed user but not removed from session", getUserFromSession(token));
    }

    
    @Test(expected = LoginBubbleDocsException.class)
    public void userToDeleteDoesNotExist() {
        new DeleteUserIntegrator(root, USERNAME_DOES_NOT_EXIST).execute();
    }

    
    @Test(expected = UnauthorizedOperationException.class)
    public void notRootUser() {
        String ars = addUserToSession(USERNAME);
        new DeleteUserIntegrator(ars, USERNAME_TO_DELETE).execute();
    }

    
    @Test(expected = UserNotInSessionException.class)
    public void rootNotInSession() {
        removeUserFromSession(root);

        new DeleteUserIntegrator(root, USERNAME_TO_DELETE).execute();
    }

    
    @Test(expected = UserNotInSessionException.class)
    public void notInSessionAndNotRoot() {
        String ars = addUserToSession(USERNAME);
        removeUserFromSession(ars);

        new DeleteUserIntegrator(ars, USERNAME_TO_DELETE).execute();
    }

    
    @Test(expected = UserNotInSessionException.class)
    public void accessUserDoesNotExist() {
        new DeleteUserIntegrator(USERNAME_DOES_NOT_EXIST, USERNAME_TO_DELETE).execute();
    }
    
}
