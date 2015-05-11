package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import pt.tecnico.bubbledocs.BubbleDocsServiceTest;

import org.junit.Test;
import org.joda.time.DateTime;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.ReferenceContent;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.exception.PositionOutOfBoundsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.exception.SpreadSheetIdUnknown;
import pt.tecnico.bubbledocs.exception.UserHasNotWriteAccessException;
import pt.tecnico.bubbledocs.exception.CellProtectedException;


public class AssignReferenceCellTest extends BubbleDocsServiceTest {

	// the tokens
	private String jp; // the token for user jp
	private String ars; // the token for user ars
	private String nwp; // the token for user nwp
	private String root; // the token for user root
	private String uu = "uu"; // the token for the unknown user

	// the perfect user
	private static final String USERNAME = "sjp";
	private static final String EMAIL = "sjp@ist.utl.pt";

	// the no session user
	private static final String USERNAME2 = "ars";
	private static final String EMAIL2 = "ars@ist.utl.pt";

	// the no write permission user
	private static final String USERNAME3 = "nwp";
	private static final String EMAIL3 = "nwp@ist.utl.pt";

	// the root user
	private static final String ROOT_USERNAME = "root";

	private static final int l1 = 1;
	private static final int c1 = 1;
	private static final int l2 = 1;
	private static final int c2 = 2;
	private static final int l3 = 20;
	private static final int c3 = 20;
	private static final int l4 = 20;
	private static final int c4 = 21;
	private static final int l5 = 3;
	private static final int c5 = 3;
	private static final int l6 = 4;
	private static final int c6 = 4;

	// the valid cell id & referenced cell id
	private static final String cell = l1 + ";" + c1;
	private static final String reference = l2 + ";" + c2;
	private static final String reference2 = l5 + ";" + c5;

	// the invalid cell id &  referenced cell id
	private static final String invalidCell = l3 + ";" + c3;
	private static final String invalidReference = l4 + ";" + c4;
	private static final String protectedCell = l6 + ";" + c6;

	private static final String SPREADNAME = "testsheet";
	private static final int lines = 10;
	private static final int columns = 10;

	// spread sheet id
	private int spread_id;

	@Override
	public void populate4Test() {

		createUser(USERNAME,"Joao Pereira",EMAIL);
		createUser(USERNAME2,"Armenio Rol Silva",EMAIL2);
		createUser(USERNAME3,"Jaquim Ambrosio",EMAIL3);

		//root = addUserToSession(ROOT_USERNAME);
		jp = addUserToSession(USERNAME);
		ars = addUserToSession(USERNAME2);	
		nwp = addUserToSession(USERNAME3);

		removeUserFromSession(ars);

		User jp_user = getUserFromUsername(USERNAME);

		SpreadSheet spreadsheet = createSpreadSheet(jp_user, SPREADNAME, lines, columns);
		spreadsheet.getCellFromString(protectedCell).setIsProtected(true);
		spread_id = spreadsheet.getId();

	}

	@Test
	public void success() {
		// valid cell id = cell

		// valid referenced cell id = reference

		// valid spreadsheet = spread_id
		
		BubbleDocs bd = BubbleDocs.getInstance();

		SpreadSheet spreadsheet = getSpreadSheetById(spread_id);

		// user exists & is in session & has permission to write = jp
		DateTime start = bd.getUserByToken(jp).getSession().getLastAccess();
		AssignReferenceCell service = new AssignReferenceCell( jp , spread_id , cell , reference );
		service.execute();
		DateTime end = bd.getUserByToken(jp).getSession().getLastAccess();
		
		String result = service.getResult();

		// check if Reference was assigned to Cell

		ReferenceContent rc = (ReferenceContent) spreadsheet.getCell(l1 , c1).getContent();
		Cell refc = rc.getReferenceCell();
		Cell c = spreadsheet.getCell(l1 , c1);

		assertNotNull("Cell Does Not Exist;", c);
		assertFalse("Error: Session time not updated.", end == start);
		assertEquals("Cell line doesn't match the given line;", c.getLine(), l1);
		assertEquals("Cell column doesn't match the given column;", c.getColumn(), c1);
		assertEquals("Referenced cell line doesn't match the given line;", refc.getLine(), l2);
		assertEquals("Referenced cell column doesn't match the given column;", refc.getColumn(), c2);
		assertTrue("Cell content isn't a reference;", c.getContent() instanceof pt.tecnico.bubbledocs.domain.ReferenceContent);
		assertEquals("Cell content has a wrong value.", result, reference);
	}

	@Test
	public void successTwice() {
		// valid cell id = cell

		// valid referenced cell id = reference

		// valid spreadsheet = spread_id

		SpreadSheet spreadsheet = getSpreadSheetById(spread_id);

		// user exists & is in session & has permission to write = jp

		AssignReferenceCell service = new AssignReferenceCell( jp , spread_id , cell , reference );
		service.execute();
		
		String result1 = service.getResult();
		
		service = new AssignReferenceCell( jp , spread_id , cell , reference2 );
		service.execute();
		
		String result2 = service.getResult();
		

		// check if Reference was assigned to Cell
		
		ReferenceContent rc = (ReferenceContent) spreadsheet.getCell(l1 , c1).getContent();
		Cell refc = rc.getReferenceCell();
		Cell c = spreadsheet.getCell(l1 , c1);

		assertNotNull("Cell Does Not Exist;", c);
		assertEquals("Cell line doesn't match the given line;", c.getLine(), l1);
		assertEquals("Cell column doesn't match the given column;", c.getColumn(), c1);
		assertEquals("Referenced cell line doesn't match the given line;", refc.getLine(), l5);
		assertEquals("Referenced cell column doesn't match the given column;", refc.getColumn(), c5);
		assertTrue("Cell content isn't a reference;", c.getContent() instanceof pt.tecnico.bubbledocs.domain.ReferenceContent);
		assertFalse("Cell content was not overwritten.", result1.equals(result2));
		assertEquals("Cell content has a wrong value.", result2, reference2);
		
		
	}

	@Test(expected = UserNotInSessionException.class)

	public void userNotInSession() {

		// valid cell id = cell

		// valid referenced cell id = reference

		// valid spread id = spread_id

		// user not in session, ars

		AssignReferenceCell service = new AssignReferenceCell( ars , spread_id , cell , reference );
		service.execute();

	}

	@Test(expected = SpreadSheetIdUnknown.class)

	public void spreadSheetIdUnknown() {

		// valid cell id = cell

		// valid referenced cell id = reference

		// invalid spread id = spread_id + 1000

		// user exists & is in session & has permission to write = jp

		AssignReferenceCell service = new AssignReferenceCell( jp , spread_id + 1000 , cell , reference );
		service.execute();

	}

	//Se o user nao estiver logado nao existe forma de saber se ele 
	//existe ou nao porque so passo o token para o service AssignReferenceCell
	
	//Deprecated -- UnknownBubbleDocsUserException ja nao e usado. 
	//Permanecera em comentario para a terceira entrega; caso nao seja necessario para o R_4, entao sera apagado.
/*
	@Test(expected = UnknownBubbleDocsUserException.class)

	public void unknownBubbleDocsUser() {

		// valid cell id = cell

		// valid referenced cell id = reference

		// valid spread id = spread_id

		// unknown bubbledocs user = uu

		AssignReferenceCell service = new AssignReferenceCell( uu , spread_id , cell , reference );
		service.execute();

	}*/

	@Test(expected = UserHasNotWriteAccessException.class)

	public void userHasNotWriteAccess() {

		// valid cell id = cell

		// valid referenced cell id = reference

		// valid spread id = spread_id

		// user has session doesn't have write access = nwa

		AssignReferenceCell service = new AssignReferenceCell( nwp , spread_id , cell , reference );
		service.execute();

	}

	@Test(expected = PositionOutOfBoundsException.class)

	public void cellOutOfBounds() {

		// invalid cell id = invalidCell

		// valid referenced cell id = reference

		// valid spread id = spread_id

		// user exists & is in session & has permission to write = jp

		AssignReferenceCell service = new AssignReferenceCell( jp , spread_id , invalidCell , reference );
		service.execute();

	}

	@Test(expected = PositionOutOfBoundsException.class)

	public void referencedCellOutOfBound() {

		// valid cell id = cell

		// invalid referenced cell id = invalidReference

		// valid spread id = spread_id

		// user exists & is in session & has permission to write = jp

		AssignReferenceCell service = new AssignReferenceCell( jp , spread_id , cell , invalidReference );
		service.execute();

	}
	
	@Test(expected = CellProtectedException.class)
	public void cellProtected() {

		// protected cell id = protectedCell

		// valid referenced cell id = reference

		// valid spreadsheet = spread_id

		// user exists & is in session & has permission to write = jp

		AssignReferenceCell service = new AssignReferenceCell( jp , spread_id , protectedCell , reference );
		service.execute();
	}

}
