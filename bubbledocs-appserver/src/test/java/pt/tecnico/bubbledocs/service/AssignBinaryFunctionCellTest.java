package pt.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.joda.time.DateTime;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.LiteralContent;
import pt.tecnico.bubbledocs.domain.BinaryFunction;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.exception.PositionOutOfBoundsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.exception.SpreadSheetIdUnknown;
import pt.tecnico.bubbledocs.exception.UserHasNotWriteAccessException;
import pt.tecnico.bubbledocs.exception.InvalidLiteralValueException;
import pt.tecnico.bubbledocs.exception.CellProtectedException;

public class AssignBinaryFunctionCellTest extends BubbleDocsServiceTest {
	
	private static final String silverToken = "Silver7";
	private String goldToken;
	private String crystalToken;
	
	private static final String silverName = "Giorgio Platina";
	private static final String goldName = "Ethan Kin";
	private static final String crystalName = "Kristina Gemma";
	
	private static final String silverUsername = "Silver";
	private static final String goldUsername = "Gold";
	private static final String crystalUsername = "Crystal";
	
	private static final String silverEmail = "seafoam_isles@johto.pk";
	private static final String goldEmail = "ecruteak_city@johto.pk";
	private static final String crystalEmail = "eusine_xoxo@johto.pk";
	
	private static final int testCellRowA = 2;
	private static final int testCellColumnA = 3;
	private static final String testCellStringA = testCellRowA + ";" + testCellColumnA;

	private static final int testCellRowB = 3;
	private static final int testCellColumnB = 2;
	private static final String testCellStringB = testCellRowB + ";" + testCellColumnB;
	
	private static final int testCellRowC = 1;
	private static final int testCellColumnC = 4;
	private static final String testCellStringC = testCellRowC + ";" + testCellColumnC;
	
	private static final int testCellRowD = 4;
	private static final int testCellColumnD = 1;
	private static final String testCellStringD = testCellRowD + ";" + testCellColumnD;
	
	private static final int testingCellRow = 1;
	private static final int testingCellColumn = 1;
	private static final String testingCellString = testingCellRow + ";" + testingCellColumn;
	
	private static final int expectedValueOne = 4;
	private static final String expectedValueOneString = "4";
	
	private static final int initValueA = 2;
	private static final int initValueB = 6;
	private static final int initValueC = 2;
	private static final int initValueD = 8;
	
	private static final String testExpressionA = "=ADD(2," + testCellStringA + ")";
	private static final String testExpressionB = "=SUB(2," + testCellStringB + ")";
	private static final String testExpressionC = "=MUL(2," + testCellStringC + ")";
	private static final String testExpressionD = "=DIV(2," + testCellStringD + ")";
	
	private static final int expectedValueTwoA = 6;
	private static final String testValueTwoStringA = "6";
	
	private static final int expectedValueTwoB = 2;
	private static final String testValueTwoStringB = "2";
	
	private static final int expectedValueTwoC = 8;
	private static final String testValueTwoStringC = "8";

	private static final int expectedValueTwoD = 2;
	private static final String testValueTwoStringD = "2";
	
	private int sheetId;
	private static final String sheetName = "ho-oh";
	private static final int sheetRows = 5;
	private static final int sheetColumns = 5;
	
	private static final int protectedCellRow = 4;
	private static final int protectedCellColumn = 3;
	private static final String protectedCellString = protectedCellRow + ";" + protectedCellColumn;
	
	private static final String invalidUser = "NotARealUser";
	private static final int fakeSheetId = 555556;
	private static final String fakeCellString = "99;45";
	private static final String fakeValueString = "KindaNotAFunction";
	
	
	/*
	 * Setup
	 */
	public void populate4Test(){
		
		/*
		 * Creates users.
		 */
		createUser(silverUsername, silverName, silverEmail);
		createUser(goldUsername, goldName, goldEmail);
		createUser(crystalUsername, crystalName, crystalEmail);
		
		/*
		 * Logs them in.
		 */
	
		goldToken = addUserToSession(goldUsername);
		crystalToken = addUserToSession(crystalUsername);
		
		
		/*
		 * Creates test Spreadsheet
		 */
		User gold = getUserFromUsername(goldUsername);
		SpreadSheet testSpread = createSpreadSheet(gold, sheetName, sheetRows, sheetColumns);
		
		testSpread.getCellFromString(protectedCellString).setIsProtected(true);
		
		testSpread.getCellFromString(testCellStringA).setLiteralContent(initValueA);
		testSpread.getCellFromString(testCellStringB).setLiteralContent(initValueB);
		testSpread.getCellFromString(testCellStringC).setLiteralContent(initValueC);
		testSpread.getCellFromString(testCellStringD).setLiteralContent(initValueD);
		
		sheetId = testSpread.getId();
		
	}
	
	@Test
	public void successAdd() {	
		
		/* Success case:
		 * 	- Valid user (exists, has write permissions, is in session)
		 * 	- Valid spreadsheet.
		 *  - Valid cell bounds.
		 *  - Valid values.
		 *  - Applies the binary function ADD to given cell.
		 */
		BubbleDocs bd = BubbleDocs.getInstance();
		AssignBinaryFunctionCell abfc = new AssignBinaryFunctionCell(goldToken, sheetId, testingCellString, testExpressionA);
		DateTime start = bd.getUserByToken(goldToken).getSession().getLastAccess();	
		abfc.execute();
		DateTime end = bd.getUserByToken(goldToken).getSession().getLastAccess();
		
		/*
		 * Compare cases
		 */
		
		SpreadSheet successSpread = getSpreadSheetById(sheetId);
		Cell successCell = successSpread.getCell(testingCellRow, testingCellColumn);
		BinaryFunction successContent = (BinaryFunction)successCell.getContent();
		int successValue = successContent.getValue();
		String successValueString = abfc.getResult();
		
		/*
		 * Tests
		 */
		
		assertFalse("Error: Session time not updated.", end == start);
		assertTrue("Error: Cell content is not an Add binary function.", successCell.getContent() instanceof pt.tecnico.bubbledocs.domain.BFAdd);
		assertNotNull("Error: Cell content does not contain a value.", successValue);
		assertEquals("Error: Literal value is not the one provided.", successValue, expectedValueOne);
		assertEquals("Error: Service return is not the one expected.", successValueString, expectedValueOneString);
		
	}
	
	@Test
	public void successSub() {	
		
		/* Success case:
		 * 	- Valid user (exists, has write permissions, is in session)
		 * 	- Valid spreadsheet.
		 *  - Valid cell bounds.
		 *  - Valid values.
		 *  - Applies the binary function SUB to given cell.
		 */
		BubbleDocs bd = BubbleDocs.getInstance();
		AssignBinaryFunctionCell abfc = new AssignBinaryFunctionCell(goldToken, sheetId, testingCellString, testExpressionB);
		DateTime start = bd.getUserByToken(goldToken).getSession().getLastAccess();	
		abfc.execute();
		DateTime end = bd.getUserByToken(goldToken).getSession().getLastAccess();
		
		/*
		 * Compare cases
		 */
		
		SpreadSheet successSpread = getSpreadSheetById(sheetId);
		Cell successCell = successSpread.getCell(testingCellRow, testingCellColumn);
		BinaryFunction successContent = (BinaryFunction)successCell.getContent();
		int successValue = successContent.getValue();
		String successValueString = abfc.getResult();
		
		/*
		 * Tests
		 */
		
		assertFalse("Error: Session time not updated.", end == start);
		assertTrue("Error: Cell content is not a Sub binary function.", successCell.getContent() instanceof pt.tecnico.bubbledocs.domain.BFSub);
		assertNotNull("Error: Cell content does not contain a value.", successValue);
		assertEquals("Error: Literal value is not the one provided.", successValue, expectedValueOne);
		assertEquals("Error: Service return is not the one expected.", successValueString, expectedValueOneString);
		
	}
	
	@Test
	public void successMul() {	
		
		/* Success case:
		 * 	- Valid user (exists, has write permissions, is in session)
		 * 	- Valid spreadsheet.
		 *  - Valid cell bounds.
		 *  - Valid values.
		 *  - Applies the binary function MUL to given cell.
		 */
		BubbleDocs bd = BubbleDocs.getInstance();
		AssignBinaryFunctionCell abfc = new AssignBinaryFunctionCell(goldToken, sheetId, testingCellString, testExpressionC);
		DateTime start = bd.getUserByToken(goldToken).getSession().getLastAccess();	
		abfc.execute();
		DateTime end = bd.getUserByToken(goldToken).getSession().getLastAccess();
		
		/*
		 * Compare cases
		 */
		
		SpreadSheet successSpread = getSpreadSheetById(sheetId);
		Cell successCell = successSpread.getCell(testingCellRow, testingCellColumn);
		BinaryFunction successContent = (BinaryFunction)successCell.getContent();
		int successValue = successContent.getValue();
		String successValueString = abfc.getResult();
		
		/*
		 * Tests
		 */
		
		assertFalse("Error: Session time not updated.", end == start);
		assertTrue("Error: Cell content is not a Mul binary function.", successCell.getContent() instanceof pt.tecnico.bubbledocs.domain.BFMul);
		assertNotNull("Error: Cell content does not contain a value.", successValue);
		assertEquals("Error: Literal value is not the one provided.", successValue, expectedValueOne);
		assertEquals("Error: Service return is not the one expected.", successValueString, expectedValueOneString);
		
	}
	
	@Test
	public void successDiv() {	
		
		/* Success case:
		 * 	- Valid user (exists, has write permissions, is in session)
		 * 	- Valid spreadsheet.
		 *  - Valid cell bounds.
		 *  - Valid values.
		 *  - Applies the binary function DIV to given cell.
		 */
		BubbleDocs bd = BubbleDocs.getInstance();
		AssignBinaryFunctionCell abfc = new AssignBinaryFunctionCell(goldToken, sheetId, testingCellString, testExpressionD);
		DateTime start = bd.getUserByToken(goldToken).getSession().getLastAccess();	
		abfc.execute();
		DateTime end = bd.getUserByToken(goldToken).getSession().getLastAccess();
		
		/*
		 * Compare cases
		 */
		
		SpreadSheet successSpread = getSpreadSheetById(sheetId);
		Cell successCell = successSpread.getCell(testingCellRow, testingCellColumn);
		BinaryFunction successContent = (BinaryFunction)successCell.getContent();
		int successValue = successContent.getValue();
		String successValueString = abfc.getResult();
		
		/*
		 * Tests
		 */
		
		assertFalse("Error: Session time not updated.", end == start);
		assertTrue("Error: Cell content is not a Mul binary function.", successCell.getContent() instanceof pt.tecnico.bubbledocs.domain.BFDiv);
		assertNotNull("Error: Cell content does not contain a value.", successValue);
		assertEquals("Error: Literal value is not the one provided.", successValue, expectedValueOne);
		assertEquals("Error: Service return is not the one expected.", successValueString, expectedValueOneString);
		
	}

	
	@Test(expected = UserNotInSessionException.class)
	public void userIsNotInSessionError() {

		/* Error case 1:
		 *  - User does not contain a valid token.
		 *  
		 *  NOTE: See the note in test #1.
		 */

		AssignBinaryFunctionCell service = new AssignBinaryFunctionCell(silverToken, sheetId, testingCellString, testExpressionA);
		service.execute();
	}
	
	@Test(expected = UserHasNotWriteAccessException.class)
	public void userHasNotWriteAccessError() {

		/* Error case 2:
		 *  - User does not contain writing permissions in the given spreadsheet.
		 */

		AssignBinaryFunctionCell service = new AssignBinaryFunctionCell(crystalToken, sheetId, testingCellString, testExpressionA);
		service.execute();
	}
	
	@Test(expected = SpreadSheetIdUnknown.class)
	public void spreadSheetIdUnknownError() {

		/* Error case 3:
		 *  - Invalid spreadsheet id.
		 */

		AssignBinaryFunctionCell service = new AssignBinaryFunctionCell(goldToken, fakeSheetId, testingCellString, testExpressionA);
		service.execute();
	}
	
	@Test(expected = PositionOutOfBoundsException.class)
	public void positionOutOfBoundsError() {

		/* Error case 4:
		 *  - Invalid cell information.
		 */

		AssignBinaryFunctionCell service = new AssignBinaryFunctionCell(goldToken, sheetId, fakeCellString, testExpressionA);
		service.execute();
	}
	
	@Test(expected = CellProtectedException.class)
	public void cellProtectedError() {

		/* Error case 5:
		 *  - Trying to write to a protected cell.
		 */

		AssignBinaryFunctionCell service = new AssignBinaryFunctionCell(goldToken, sheetId, protectedCellString, testExpressionA);
		service.execute();
	}
	
	
}