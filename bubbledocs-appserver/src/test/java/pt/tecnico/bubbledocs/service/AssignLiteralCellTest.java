package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.joda.time.DateTime;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.LiteralContent;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.exception.PositionOutOfBoundsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.exception.SpreadSheetIdUnknown;
import pt.tecnico.bubbledocs.exception.UserHasNotWriteAccessException;
import pt.tecnico.bubbledocs.exception.InvalidLiteralValueException;
import pt.tecnico.bubbledocs.exception.CellProtectedException;

public class AssignLiteralCellTest extends BubbleDocsServiceTest {
	
	private static final String redToken = "Red4";
	private String goldToken;
	private String greenToken;
	
	private static final String redName = "Redd White";
	private static final String goldName = "Goldilocks Southey";
	private static final String greenName = "Giuseppe Verdi";
	
	private static final String redUsername = "Red";
	private static final String goldUsername = "Gold";
	private static final String greenUsername = "Green";
	
	private static final String redPassword = "cinnabar";
	private static final String goldPassword = "goldenrod";
	private static final String greenPassword = "viridian";
	
	private static final int testValueOne = 3;
	private static final String testValueOneString = "3";
	
	private static final int testValueTwo = 5;
	private static final String testValueTwoString = "5";
	
	private int sheetId;
	private static final String sheetName = "chameleon";
	private static final int sheetRows = 5;
	private static final int sheetColumns = 5;
	
	private static final int testCellRow = 2;
	private static final int testCellColumn = 3;
	private static final String testCellString = testCellRow + ";" + testCellColumn;
	
	private static final int protectedCellRow = 4;
	private static final int protectedCellColumn = 3;
	private static final String protectedCellString = protectedCellRow + ";" + protectedCellColumn;
	
	private static final String invalidUser = "NotARealUser";
	private static final int fakeSheetId = 555556;
	private static final String fakeCellString = "99;45";
	private static final String fakeValueString = "KindaNotANumber";
	
	
	/*
	 * Setup
	 */
	public void populate4Test(){
		
		/*
		 * Creates users.
		 */
		createUser(redUsername, redPassword, redName);
		createUser(goldUsername, goldPassword, goldName);
		createUser(greenUsername, greenPassword, greenName);
		
		/*
		 * Logs them in.
		 */
	
		goldToken = addUserToSession(goldUsername);
		greenToken = addUserToSession(greenUsername);
		
		
		/*
		 * Creates test Spreadsheet
		 */
		User gold = getUserFromUsername(goldUsername);
		SpreadSheet testSpread = createSpreadSheet(gold, sheetName, sheetRows, sheetColumns);
		
		testSpread.getCellFromString(protectedCellString).setIsProtected(true);
		
		sheetId = testSpread.getId();
		
	}
	
	@Test
	public void success() {	
		
		/* Success case:
		 * 	- Valid user (exists, has write permissions, is in session)
		 * 	- Valid spreadsheet.
		 *  - Valid cell bounds.
		 *  - Valid value.
		 *  - Creates a literal content in given cell.
		 */
		BubbleDocs bd = BubbleDocs.getInstance();
		AssignLiteralCell alc = new AssignLiteralCell(goldToken, sheetId, testCellString, testValueOneString);
		DateTime start = bd.getUserByToken(goldToken).getSession().getLastAccess();	
		alc.execute();
		DateTime end = bd.getUserByToken(goldToken).getSession().getLastAccess();
		
		/*
		 * Compare cases
		 */
		
		SpreadSheet successSpread = getSpreadSheetById(sheetId);
		Cell successCell = successSpread.getCell(testCellRow, testCellColumn);
		LiteralContent successContent = (LiteralContent)successCell.getContent();
		int successValue = successContent.getValue();
		String successValueString = alc.getResult();
		
		/*
		 * Tests
		 */
		
		assertFalse("Error: Session time not updated.", end == start);
		assertTrue("Error: Cell content is not a literal.", successCell.getContent() instanceof pt.tecnico.bubbledocs.domain.LiteralContent);
		assertNotNull("Error: Cell content does not contain a value.", successValue);
		assertEquals("Error: Literal value is not the one provided.", successValue, testValueOne);
		assertEquals("Error: Service return is not the one expected.", successValueString, testValueOneString);
		
	}
	
	@Test
	public void successTwice() {	
		
		/* successTwice case:
		 * 	- Valid user (exists, has write permissions, is in session)
		 * 	- Valid spreadsheet.
		 *  - Valid cell bounds.
		 *  - Valid value1.
		 *  - Valid value2.
		 *  - Creates a literal content in given cell, then replaces that value with a new one.
		 */
		
	
		/*
		 * First assignment
		 */
		AssignLiteralCell alc = new AssignLiteralCell(goldToken, sheetId, testCellString, testValueOneString);
		alc.execute();

		
		/*
		 * Compare cases
		 */
		
		SpreadSheet successSpread = getSpreadSheetById(sheetId);
		Cell successCell = successSpread.getCell(testCellRow, testCellColumn);
		LiteralContent successContentOne = (LiteralContent)successCell.getContent();
		int successValueOne = successContentOne.getValue();
		
		
		/*
		 * Second assignment
		 */
		
		alc = new AssignLiteralCell(goldToken, sheetId, testCellString, testValueTwoString);
		alc.execute();
		
		/*
		 * More compare cases
		 */
		
		successCell = successSpread.getCell(testCellRow, testCellColumn);
		LiteralContent successContentTwo = (LiteralContent)successCell.getContent();
		int successValueTwo = successContentTwo.getValue();
		String successValueTwoString = alc.getResult();
		
		/*
		 * Tests
		 */
		
		assertTrue("Error: Cell content is not a literal.", successCell.getContent() instanceof pt.tecnico.bubbledocs.domain.LiteralContent);
		assertNotNull("Error: Cell content does not have a value.", successValueTwo);
		assertFalse("Error: Did not overwrite value.", (successValueOne == successValueTwo));
		assertEquals("Error: Literal value is not the one provided.", successValueTwo, testValueTwo);
		assertEquals("Error: Service return is not the one expected.", successValueTwoString, testValueTwoString);
		
	}
	
	
	/*@Test(expected = UnknownBubbleDocsUserException.class)
	public void unknownBubbleDocsUserError() {

		/* Error case 1 (Deprecated for now):
		 *  - User does not exist.
		 *  
		 *  NOTE: Not entirely sure on this one; a made-up token can either mean an unknown user or a user without a session, so...
		 *  Currently we're relying on the fact that the token is compromised of the username + a digit to figure out the original
		 *  username and then checking if that exists. Once a more complex token is created, this method will cease to work. Will need
		 *  to confirm with teacher after this second checkpoint.
		 *  
		 *  NOTE2: With the R_3 submission, UnknownBubbleDocsUserException is no longer used. Assuming this is no longer necessary to check.
		 *  Keeping it in case it becomes necessary again -- otherwise will be removed in R_4.
		 */
	/*
		AssignLiteralCell service = new AssignLiteralCell(invalidUser, sheetId, testCellString, testValueOneString);
		service.execute();
	}*/
	
	@Test(expected = UserNotInSessionException.class)
	public void userIsNotInSessionError() {

		/* Error case 2:
		 *  - User does not contain a valid token.
		 *  
		 *  NOTE: See the note in test #1.
		 */

		AssignLiteralCell service = new AssignLiteralCell(redToken, sheetId, testCellString, testValueOneString);
		service.execute();
	}
	
	@Test(expected = UserHasNotWriteAccessException.class)
	public void userHasNotWriteAccessError() {

		/* Error case 3:
		 *  - User does not contain writing permissions in the given spreadsheet.
		 */

		AssignLiteralCell service = new AssignLiteralCell(greenToken, sheetId, testCellString, testValueOneString);
		service.execute();
	}
	
	@Test(expected = SpreadSheetIdUnknown.class)
	public void spreadSheetIdUnknownError() {

		/* Error case 4:
		 *  - Invalid spreadsheet id.
		 */

		AssignLiteralCell service = new AssignLiteralCell(goldToken, fakeSheetId, testCellString, testValueOneString);
		service.execute();
	}
	
	@Test(expected = PositionOutOfBoundsException.class)
	public void positionOutOfBoundsError() {

		/* Error case 5:
		 *  - Invalid cell information.
		 */

		AssignLiteralCell service = new AssignLiteralCell(goldToken, sheetId, fakeCellString, testValueOneString);
		service.execute();
	}
	
	@Test(expected = InvalidLiteralValueException.class)
	public void invalidLiteralValueError() {

		/* Error case 6:
		 *  - Invalid value.
		 */

		AssignLiteralCell service = new AssignLiteralCell(goldToken, sheetId, testCellString, fakeValueString);
		service.execute();
	}
	
	@Test(expected = CellProtectedException.class)
	public void cellProtectedError() {

		/* Error case 7:
		 *  - Trying to write to a protected cell.
		 */

		AssignLiteralCell service = new AssignLiteralCell(goldToken, sheetId, protectedCellString, testValueOneString);
		service.execute();
	}
	
	
}