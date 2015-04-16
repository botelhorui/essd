package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

// add needed import declarations

public class CreateUserTest extends BubbleDocsServiceTest {

    // the tokens
    private String rootToken;
    private String arsToken;

    private static final String USERNAME = "ars";
    private static final String PASSWORD = "ars";
    private static final String ROOT_USERNAME = "root";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";
    private static final String EMAIL = "no.one@yahoo.com";

    @Override
    public void populate4Test() {
    	
        createUser(PASSWORD,USERNAME,"António Rito Silva");
        rootToken = addUserToSession(ROOT_USERNAME);
        arsToken = addUserToSession(USERNAME);
    }

    @Test
    public void success() {
        CreateUser service = new CreateUser(rootToken, USERNAME_DOES_NOT_EXIST, "José Ferreira", EMAIL);
        service.execute();

	// User is the domain class that represents a User
        User user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);

        assertEquals(USERNAME_DOES_NOT_EXIST, user.getUsername());
        assertEquals(EMAIL, user.getEmail());
        assertEquals("José Ferreira", user.getName());
    }

    @Test(expected = DuplicateUsernameException.class)
    public void usernameExists() {
        CreateUser service = new CreateUser(rootToken, USERNAME, "jose",
                "José Ferreira");
        service.execute();
    }

    @Test(expected = EmptyUsernameException.class)
    public void emptyUsername() {
        CreateUser service = new CreateUser(rootToken, "", "jose", "José Ferreira");
        service.execute();
    }

    @Test(expected = UnauthorizedOperationException.class)
    public void unauthorizedUserCreation() {
        CreateUser service = new CreateUser(arsToken, USERNAME_DOES_NOT_EXIST, "jose",
                "José Ferreira");
        service.execute();
    }

    @Test(expected = UserNotInSessionException.class)
    public void accessUsernameNotExist() {
        removeUserFromSession(rootToken);
        CreateUser service = new CreateUser(rootToken, USERNAME_DOES_NOT_EXIST, "jose",
                "José Ferreira");
        service.execute();
    }

}
