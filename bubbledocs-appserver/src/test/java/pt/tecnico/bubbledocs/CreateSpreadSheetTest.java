package pt.tecnico.bubbledocs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;


public class CreateUserTest extends AbstractTest {

    protected void populateDomain() {
		User pf = bd.createUser("pf","sub","Paul Door");
    }

    

    @Test
    public void success() {
    	
    	SpreadSheet ss = pf.createSheet("testsheet", 80, 80);
    	
        assertNotNull("Spreadsheet wasn't created", ss);
        
        assertEquals("Columns number doesnt match", 80, ss.getColumns());
        assertEquals("Lines number doesnt match", 80, ss.getLines());
        
    }

    

    

    

}