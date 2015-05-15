package pt.tecnico.bubbledocs.integration.system;

import java.util.Arrays;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import pt.tecnico.bubbledocs.integration.AssignBinaryFunctionIntegrator;
import pt.tecnico.bubbledocs.integration.AssignLiteralCellIntegrator;
import pt.tecnico.bubbledocs.integration.CreateUserIntegrator;
import pt.tecnico.bubbledocs.integration.ImportDocumentIntegrator;
import pt.tecnico.bubbledocs.integration.LoginUserIntegrator;
import pt.tecnico.bubbledocs.integration.CreateSpreadsheetIntegrator;
import pt.tecnico.bubbledocs.integration.ExportDocumentIntegrator;
import pt.tecnico.bubbledocs.service.AssignLiteralCell;
import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.service.GetSpreadSheetContent;
import pt.tecnico.bubbledocs.service.LoginUser;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;
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
import pt.ulisboa.tecnico.sdis.id.ws.cli.SDIdClient;
import pt.ulisboa.tecnico.sdis.store.ws.cli.SDStoreClient;

public class LocalSystemTest extends BubbleDocsTest {

	private static final String ROOT = "root";
	private static final String USERNAME = "alice";
	private static final String NAME ="alice";
	private static final String EMAIL = "alice@xxx";
	private static final String PASSWORD = "Aaa1";
	private static final String DOC_NAME = "testsheet";

	private String rootToken;

	private static BubbleDocs bd;
	// executes before each test
	@Before 
	@Atomic
	public void setUp() throws Exception {
		bd = BubbleDocs.getInstance();
		populate4Test();
	}


	// executes after each test
	@After
	@Atomic
	public void tearDown() {
		bd.delete();
	}

	// populates domain for tests
	@Atomic
	public void populate4Test() {		
		LoginUser rootLogin = new LoginUser(ROOT, ROOT);
		rootLogin.execute();
		rootToken = rootLogin.getUserToken();
		//userToken = addUserToSession(USERNAME);
	}
	
	@Test
	public void success(@Mocked final IDRemoteServices id,@Mocked final StoreRemoteServices store) {	
		String userToken;
		SpreadSheet spreadsheet;
		
		CreateUserIntegrator createUser = new CreateUserIntegrator( rootToken, USERNAME, NAME, EMAIL);
		new Expectations() {{		
			id.createUser(USERNAME, NAME, EMAIL);
		}};
		createUser.execute();
		
		
		
		LoginUserIntegrator userLogin = new LoginUserIntegrator(USERNAME, PASSWORD);
		new Expectations() {{		
			id.loginUser(USERNAME,PASSWORD);
		}};
		userLogin.execute();
		userToken = userLogin.getUserToken();
		
		
		
		CreateSpreadsheetIntegrator createSheet = new CreateSpreadsheetIntegrator(userToken,DOC_NAME , 5, 5);
		createSheet.execute();
		final int sheetId = createSheet.getSheetId();
		
		GetSpreadSheetContent gsc = new GetSpreadSheetContent(userToken, sheetId);
		gsc.execute();
		gsc.printMatrix();
		

		AssignLiteralCellIntegrator alci = new AssignLiteralCellIntegrator(userToken, sheetId, "1;1", "1");
		alci.execute();
		
		gsc = new GetSpreadSheetContent(userToken, sheetId);
		gsc.execute();
		gsc.printMatrix();		
		
	
		new Expectations() {{		
			store.storeDocument(anyString, anyString, (byte[])any);
		}};			
		final ExportDocumentIntegrator exportDocument = new ExportDocumentIntegrator(userToken,createSheet.getSheetId());
		exportDocument.execute();		
		System.out.println("Exported :"+new String(exportDocument.getBytes()));
		
		new Expectations() {{		
			store.loadDocument(anyString, anyString); result = exportDocument.getBytes();
		}};		
		ImportDocumentIntegrator importDocument = new ImportDocumentIntegrator(userToken, createSheet.getSheetId());
		importDocument.execute();
		
		gsc = new GetSpreadSheetContent(userToken, importDocument.get_docId());
		gsc.execute();
		gsc.printMatrix();
	}
}
