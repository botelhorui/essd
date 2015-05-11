package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;

import pt.tecnico.bubbledocs.BubbleDocsServiceTest;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Content;
import pt.tecnico.bubbledocs.domain.ReferenceContent;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.SpreadSheetIdUnknown;
import pt.tecnico.bubbledocs.exception.UserHasNotReadAccessException;
import pt.tecnico.bubbledocs.exception.UserHasNotWriteAccessException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.integration.GetSheetContentIntegrator;
import pt.tecnico.bubbledocs.service.AssignReferenceCell;

public class GetSheetContentIntegratorTest extends BubbleDocsServiceTest {
	
	// the tokens
		private String jp; // the token for user jp
		private String ars; // the token for user ars
		private String nwp; // the token for user nwp
		// the perfect user
		private static final String USERNAME = "joaop";
		private static final String PASSWORD = "jp#";

		// the no session user
		private static final String USERNAME2 = "ars";
		private static final String PASSWORD2 = "ars#";

		// the no write permission user
		private static final String USERNAME3 = "nwp";
		private static final String PASSWORD3 = "nwp#";

		private static final String SPREADNAME = "testsheet";
		

		// spread sheet id
		private int spread_id;
	
		@Override
		public void populate4Test() {

			createUser(USERNAME,PASSWORD,"Joao Pereira");
			createUser(USERNAME2,PASSWORD2,"Armenio Rol Silva");
			createUser(USERNAME3,PASSWORD3,"Jaquim Ambrosio");

			//root = addUserToSession(ROOT_USERNAME);
			jp = addUserToSession(USERNAME);
			ars = addUserToSession(USERNAME2);	
			nwp = addUserToSession(USERNAME3);

			removeUserFromSession(ars);

			User jp_user = getUserFromUsername(USERNAME);

			
			
			SpreadSheet spreadsheet = jp_user.createSheet(SPREADNAME, 2, 2);
			spreadsheet.getCell(1, 1).setLiteralContent(3);
			spreadsheet.getCell(1, 2).setReferenceContent(spreadsheet.getCell(1, 1));

		}
		
		@Test
		public void success() {
			
			
			BubbleDocs bd = BubbleDocs.getInstance();

			SpreadSheet spreadsheet = getSpreadSheetById(spread_id);
			
			
			GetSheetContentIntegrator service = new GetSheetContentIntegrator(jp , spread_id);
			service.execute();
			
			
			String[][] result = service.getResult();

			
			Cell c = spreadsheet.getCell(1, 1);
			Cell rc = spreadsheet.getCell(1, 2);
			Cell ec1 = spreadsheet.getCell(2, 1);
			Cell ec2 = spreadsheet.getCell(2, 2);
			
			assertEquals("Cell content doesnt match", Integer.toString(c.getValue()), result[1][1]);
			assertEquals("Cell content doesnt match", Integer.toString(rc.getValue()), result[1][2]);
			assertEquals("Cell content doesnt match", "", result[2][1]);
			assertEquals("Cell content doesnt match", "", result[2][2]);
			
			
		}
		
		
		@Test
		public void successtwice() {
			
			
			BubbleDocs bd = BubbleDocs.getInstance();

			SpreadSheet spreadsheet = getSpreadSheetById(spread_id);
			
			
			GetSheetContentIntegrator service = new GetSheetContentIntegrator(jp , spread_id);
			service.execute();
			
			service = new GetSheetContentIntegrator(jp , spread_id);
			service.execute();
			
			
			String[][] result = service.getResult();

			
			Cell c = spreadsheet.getCell(1, 1);
			Cell rc = spreadsheet.getCell(1, 2);
			Cell ec1 = spreadsheet.getCell(2, 1);
			Cell ec2 = spreadsheet.getCell(2, 2);
			
			assertEquals("Cell content doesnt match", Integer.toString(c.getValue()), result[1][1]);
			assertEquals("Cell content doesnt match", Integer.toString(rc.getValue()), result[1][2]);
			assertEquals("Cell content doesnt match", "", result[2][1]);
			assertEquals("Cell content doesnt match", "", result[2][2]);
			
			
		}
		
		
		@Test(expected = UserNotInSessionException.class)

		public void userNotInSession() {

			
			GetSheetContentIntegrator service = new GetSheetContentIntegrator(jp , spread_id);
			service.execute();

		}
		
		
		@Test(expected = UserHasNotReadAccessException.class)

		public void userHasNotReadAccess() {

			GetSheetContentIntegrator service = new GetSheetContentIntegrator(nwp , spread_id);
			service.execute();

		}
		
		@Test(expected = SpreadSheetIdUnknown.class)

		public void spreadsheetIdUnknown() {

			GetSheetContentIntegrator service = new GetSheetContentIntegrator(nwp , 123);
			service.execute();

		}


}
