package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import pt.tecnico.bubbledocs.BubbleDocsServiceTest;

import org.junit.Test;
import org.joda.time.DateTime;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.exception.PositionOutOfBoundsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

public class CreateSpreadSheetTest extends BubbleDocsServiceTest {
	
	// the tokens
	private String jp; // the token for user jp
	private String ars; // the token for user ars
	private String root; // the token for user root

	private static final String USERNAME = "jp";
	private static final String PASSWORD = "jp";
	private static final String USERNAME2 = "ars";
    private static final String PASSWORD2 = "ars";
	private static final String ROOT_USERNAME = "root";
	
	@Override
    public void populate4Test() {
		createUser(USERNAME,PASSWORD,"João Pereira");
		createUser(USERNAME2,PASSWORD2,"Armenio Rol Silva");
		root = addUserToSession(ROOT_USERNAME);
        jp = addUserToSession(USERNAME);
        ars = addUserToSession(USERNAME2);
		removeUserFromSession(ars);
    }
	

	
	@Test
    public void success() {
        
		BubbleDocs bd = BubbleDocs.getInstance();
		DateTime start = bd.getUserByToken(jp).getSession().getLastAccess();
		CreateSpreadSheet service = new CreateSpreadSheet(jp, "testsheet", 20, 20);
		service.execute();
		DateTime end = bd.getUserByToken(jp).getSession().getLastAccess();
		int id = service.getSheetId();
		SpreadSheet sp = getSpreadSheetById(id);
		assertNotNull("SpreadSheet was not created", sp);
		assertFalse("Error: Session time not updated.", end == start);
		assertEquals("Number of lines doesnt match", sp.getLines(), 20);
		assertEquals("Number of columns doesnt match",sp.getColumns(), 20);
		assertEquals("Name doesnt match", sp.getName(), "testsheet");
		
    }
	
	
	@Test
    public void successTwice() {
		
		CreateSpreadSheet service = new CreateSpreadSheet(jp, "testsheet", 20, 20);
		CreateSpreadSheet service2 = new CreateSpreadSheet(jp, "testsheet", 20, 20);
		
		service.execute();
		service2.execute();
		
		int id = service.getSheetId();
		int id2 = service2.getSheetId();
		SpreadSheet sp = getSpreadSheetById(id);
		SpreadSheet sp2 = getSpreadSheetById(id2);
		
		assertNotNull("First SpreadSheet was not created", sp);
		assertNotNull("Second SpreadSheet was not created", sp2);
		assertTrue("Id are not different", id!=id2);
		assertEquals("Names aren't equal", sp.getName(), sp2.getName());
		
		
        
    }
	
	 @Test(expected = PositionOutOfBoundsException.class)
	    public void negativeNumberLines() {
		 CreateSpreadSheet service = new CreateSpreadSheet(jp, "testsheet", -20, 20);
		 service.execute();
		 
	    }
	 
	 @Test(expected = PositionOutOfBoundsException.class)
	    public void negativeNumberColumns() {
		 CreateSpreadSheet service = new CreateSpreadSheet(jp, "testsheet", 20, -20);
		 service.execute();
		 
	    }
	 
	 @Test(expected = PositionOutOfBoundsException.class)
	    public void zeroNumberLines() {
		 CreateSpreadSheet service = new CreateSpreadSheet(jp, "testsheet", 0, 20);
		 service.execute();
		 
	    }
	 
	 @Test(expected = PositionOutOfBoundsException.class)
	    public void zeroNumberColumns() {
		 CreateSpreadSheet service = new CreateSpreadSheet(jp, "testsheet", 20, 0);
		 service.execute();
		 
	    }
	
	 
	 @Test(expected = UserNotInSessionException.class)
	 
	    public void userNotInSession() {
		 CreateSpreadSheet service = new CreateSpreadSheet(ars, "testsheet", 20, 20);
		 service.execute();
		 
	    }

}
