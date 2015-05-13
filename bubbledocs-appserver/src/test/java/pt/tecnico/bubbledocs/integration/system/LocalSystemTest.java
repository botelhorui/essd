package pt.tecnico.bubbledocs.integration.system;

import org.junit.Test;

import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

import pt.tecnico.bubbledocs.integration.CreateUserIntegrator;
import pt.tecnico.bubbledocs.integration.LoginUserIntegrator;
import pt.tecnico.bubbledocs.integration.CreateSpreadsheetIntegrator;
import pt.tecnico.bubbledocs.integration.ExportDocumentIntegrator;

import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.domain.SpreadSheet;

import pt.ist.fenixframework.core.WriteOnReadError;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import pt.tecnico.bubbledocs.BubbleDocsTest;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.Atomic;

import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class LocalSystemTest extends BubbleDocsTest {

	@Mocked
	private IDRemoteServices remoteService;

	private static final String ROOT = "root";

	private static final String USERNAME = "treeko";
	private static final String EMAIL = "treeko@ist.utl.pt";
	private static final String PASSWORD = "grass";

	private String rootToken;
	private String userToken;
	
	private SpreadSheet spreadsheet;
	
	private static BubbleDocs bd;
	
	// executes before all tests
	@BeforeClass
	@Atomic
	public static void setUpOnce() throws Exception {

		bd = BubbleDocs.getInstance();

	}
	
	// executes before each test
	@Before 
	public void setUp() throws Exception {

		populate4Test();
		
	}

	
	
	// executes after each test
	@After
	
	public void tearDown() {


	}

	// executes in the very end of all the tests
	@AfterClass
	@Atomic
	public static void tearDownOnce() {

		bd.delete();

	}
	
	// populates domain for tests
	public void populate4Test() {

		/*createUser(USERNAME,"Treeko Grass", EMAIL);
		rootToken = addUserToSession(ROOT);
		userToken = addUserToSession(USERNAME);*/
		
		bd.cenas();

	}
	@Test
	public void success() {

		
		/*// Create User Integrator
		
		CreateUserIntegrator createUserService = new CreateUserIntegrator( rootToken,  USERNAME, "Treeko Grass",  EMAIL );
		// Expectations : Expected behaviour when class is called with specific arguments
		new Expectations() {{			
			remoteService.createUser( USERNAME, "Treeko Grass", EMAIL );
		}};
		createUserService.execute();	
		
		
		
		userToken = addUserToSession(USERNAME);
		
		
		
		// Login User Integrator
		
		LoginUserIntegrator loginService = new LoginUserIntegrator( USERNAME, PASSWORD);
		new Expectations() {{
			remoteService.loginUser( USERNAME, PASSWORD);
		}};
		loginService.execute();
		
		
		// Create SpreadSheet
		
		CreateSpreadsheetIntegrator spreadsheetService = new CreateSpreadsheetIntegrator(userToken, "testsheet", 20, 20);
		spreadsheetService.execute();
				
		spreadsheet = getSpreadSheet("testsheet");
		
		
		
		// ExportDocumentIntegrator
		
		ExportDocumentIntegrator exportService = new ExportDocumentIntegrator(userToken, spreadsheet.getId());
		exportService.execute();
		
		
		// ImportDocumentIntegrator

		// TODO
		
		
		// Assign Binary Function Integrator
		
		// TODO
		
		
		// Assign Literal Cell Integrator
		
		// TODO
		
		
		// Assign Reference Cell Integrator
		
		// TODO
	*/	
	}

}
