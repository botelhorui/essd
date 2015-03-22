package pt.tecnico.bubbledocs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;

public class CreateUserTest extends AbstractTest {

    protected void populateDomain() {
		
    }

    

    @Test
    public void success() {
    	
    	User pf = bd.createUser("pf","sub","Paul Door");
        assertTrue("User wasn't created", bd.hasUser("pf"));
        User u = bd.getUserByUsername("pf");
        assertEquals("Password doesnt get stored correctly", "sub", u.getPassword());
        assertEquals("Name doesnt get stored correctly", "Paul Door", u.getName());
        
    }

    

    @Test(expected = DuplicateUsernameException.class)
    public void invalidUserCreationWithDuplicateName() {
       
    	User pf = bd.createUser("pf","sub","Paul Door");
    }

    

}