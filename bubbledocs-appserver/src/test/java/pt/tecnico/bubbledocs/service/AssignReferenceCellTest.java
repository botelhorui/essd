package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.ReferenceContent;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.domain.SpreadSheet;

import pt.tecnico.bubbledocs.exception.PositionOutOfBoundsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;


public class AssignReferenceCellTest extends BubbleDocsServiceTest {
	
	// the tokens
	private String jp; // the token for user jp
	private String ars; // the token for user ars
	private String root; // the token for user root

	private static final String USERNAME = "jp";
	private static final String PASSWORD = "jp#";
	private static final String USERNAME2 = "ars";
    private static final String PASSWORD2 = "ars#";
	private static final String ROOT_USERNAME = "root";
	
	private static final String SPREADNAME = "testsheet";
	private static final int lines = 10;
	private static final int columns = 10;
	
	private static final int l1 = 1;
	private static final int c1 = 1;
	
	private static final int l2 = 1;
	private static final int c2 = 2;
	
	private int spread_id;
	
	@Override
    public void populate4Test() {
		
		createUser(USERNAME,PASSWORD,"Joao Pereira");
		createUser(USERNAME2,PASSWORD2,"Armenio Rol Silva");
		
		root = addUserToSession(ROOT_USERNAME);
        
		jp = addUserToSession(USERNAME);
        
        ars = addUserToSession(USERNAME2);		
        removeUserFromSession(ars);
        
        User jp_user = getUserFromUsername(jp);
 
        SpreadSheet spreadsheet = createSpreadSheet(jp_user, SPREADNAME, lines, columns);
        spread_id = spreadsheet.getId();
        
    }
	
	@Test
    public void success() {
        
		// valid cell id
		final String cell = l1 + ";" + c1;
				
		// valid referenced cell id
		final String reference = l2 + ";" + c2;
		
		// valid spreadsheet
		SpreadSheet spreadsheet = getSpreadSheetById(spread_id);
		
		// user exists & is in session & has permission to write = jp
		
		AssignReferenceCell service = new AssignReferenceCell( jp , spread_id , cell , reference );
		service.execute();
		
		// check if Reference was assigned to Cell
		
		Cell c = spreadsheet.getCell(l1 , c1);
		
		assertNotNull("Cell Does Not Exist;", c);
		assertEquals("Cell line doesn't match the given line;", c.getLine(), l1);
		assertEquals("Cell column doesn't match the given column;", c.getColumn(), c1);
		assertTrue("Cell content isn't a reference;", c.getContent() instanceof pt.tecnico.bubbledocs.domain.ReferenceContent);
    }
	
	@Test(expected = UserNotInSessionException.class)
	 
	    public void userNotInSession() {
		 CreateSpreadSheet service = new CreateSpreadSheet(ars, "testsheet", 20, 20);
		 service.execute();
		 
	    }

}
