package pt.tecnico.bubbledocs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.domain.BubbleDocs;


public class CreateSpreadSheetTest extends AbstractTest {

    protected void populateDomain() {
    	BubbleDocs bd = BubbleDocs.getInstance();
		User pf = bd.createUser("pf","sub","Paul Door");
    }

    

    @Test
    public void success() {
    	
    	BubbleDocs bd = BubbleDocs.getInstance();
    	
    	SpreadSheet s = bd.getUserByUsername("pf").createSheet("testsheet", 80, 80);
    	
        assertNotNull("Spreadsheet wasn't created", s);
        
        assertEquals("Columns number doesnt match", 80, s.getColumns());
        assertEquals("Lines number doesnt match", 80, s.getLines());
        
    }

    

    

    

}