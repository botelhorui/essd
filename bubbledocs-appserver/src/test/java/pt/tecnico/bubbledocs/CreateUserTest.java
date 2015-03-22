package pt.tecnico.bubbledocs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;

public class CreateUserTest extends AbstractTest {

    protected void populateDomain() {
		User pf = bd.createUser("pf","sub","Paul Door");
    }

    

    @Test
    public void success() {
        
    }

    

    @Test(expected = DuplicateUsernameException.class)
    public void invalidUserCreationWithDuplicateName() {
       
    }

    

}