package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import mockit.Expectations;
import mockit.Mocked;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.junit.Test;

import pt.tecnico.bubbledocs.BubbleDocsServiceTest;
import pt.tecnico.bubbledocs.domain.Argument;
import pt.tecnico.bubbledocs.domain.BinaryFunction;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Content;
import pt.tecnico.bubbledocs.domain.LiteralArgument;
import pt.tecnico.bubbledocs.domain.LiteralContent;
import pt.tecnico.bubbledocs.domain.ReferenceArgument;
import pt.tecnico.bubbledocs.domain.ReferenceContent;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.CannotLoadDocumentException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.integration.ImportDocumentIntegrator;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

public class ImportDocumentIntegratorTest extends BubbleDocsServiceTest {
	
	
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
			private byte[] originalDocument;
			private SpreadSheet spreadsheet;
			
			@Mocked StoreRemoteServices storeService;
			
			
			private boolean sameSpreadSheet(SpreadSheet s1,SpreadSheet s2){
				assertTrue(s1.getId()!=s2.getId());
				assertTrue(s1.getName().equals(s2.getName()));
				assertTrue(s1.getCreationDate().equals(s2.getCreationDate()));
				assertTrue(s1.getLines()==s2.getLines());
				assertTrue(s1.getColumns()==s2.getColumns());
				for(int i=1;i<=2;i++)
					for(int j=1;j<=2;j++){
						Cell c1 = s1.getCell(i, j);
						Cell c2 = s2.getCell(i, j);
						sameCell(c1,c2);			
				}				
				return true;		
			}
			
			private boolean sameCell(Cell c1,Cell c2){		
				assertTrue(c1.getLine()==c2.getLine());
				assertTrue(c1.getColumn()==c2.getColumn());
				if(!sameContent(c1.getContent(),c2.getContent()))
					return false;
				return true;
			}
			
			private boolean sameContent(Content c1, Content c2) {
				assertTrue(c1.getClass().equals(c2.getClass()));
				if(c1.getClass().equals(LiteralContent.class)){
					LiteralContent lc1 = (LiteralContent)c1;
					LiteralContent lc2 = (LiteralContent)c2;
					assertTrue(lc1.getValue()==lc2.getValue());
				}else if(c1.getClass().equals(ReferenceContent.class)){
					ReferenceContent rc1 = (ReferenceContent)c1;
					ReferenceContent rc2 = (ReferenceContent)c2;			
					assertTrue(sameCell(rc1.getReferenceCell(),rc2.getReferenceCell()));
				}else if(c1 instanceof BinaryFunction){
					BinaryFunction bf1 = (BinaryFunction)c1;
					BinaryFunction bf2 = (BinaryFunction)c2;
					if(!sameArgument(bf1.getLeftArgument(),bf2.getLeftArgument()))
						return false;
					if(!sameArgument(bf1.getRightArgument(),bf2.getRightArgument())){
						return false;
					}
				}else{
					throw new BubbleDocsException("Cell Content is not supported");
				}
				
				return true;
			}

			private boolean sameArgument(Argument a1, Argument a2) {
				assertTrue(a1.getClass().equals(a2.getClass()));
				if(a1.getClass().equals(LiteralArgument.class)){
					LiteralArgument la1 = (LiteralArgument)a1;
					LiteralArgument la2 = (LiteralArgument)a2;
					assertTrue(la1.getValue()==la2.getValue());
				}else if(a1.getClass().equals(ReferenceArgument.class)){
					ReferenceArgument ra1 = (ReferenceArgument)a1;
					ReferenceArgument ra2 = (ReferenceArgument)a2;
					assertTrue(sameCell(ra1.getReferenceCell(),ra2.getReferenceCell()));
				}else{
					throw new BubbleDocsException("BinaryFunction argument type is not supported");
				}	
				return true;
			}
		
			@Override
			public void populate4Test() {

				createUser(USERNAME,PASSWORD,"Joao");
				createUser(USERNAME2,PASSWORD2,"Armenio");
				createUser(USERNAME3,PASSWORD3,"Jaquim");

				//root = addUserToSession(ROOT_USERNAME);
				jp = addUserToSession(USERNAME);
				ars = addUserToSession(USERNAME2);	
				nwp = addUserToSession(USERNAME3);

				removeUserFromSession(ars);

				User jp_user = getUserFromUsername(USERNAME);

				
				
				spreadsheet = jp_user.createSheet(SPREADNAME, 2, 2);
				spreadsheet.getCell(1, 1).setLiteralContent(3);
				spreadsheet.getCell(1, 2).setReferenceContent(spreadsheet.getCell(1, 1));
				spreadsheet.getCell(2, 1).setLiteralContent(3);
				spreadsheet.getCell(2, 2).setLiteralContent(3);
				
				try {
					originalDocument = spreadsheet.spreadtoBytes();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				
			}
			
			
			@Test
			public void success() throws JDOMException, IOException {
				
				
				BubbleDocs bd = BubbleDocs.getInstance();
				
				
				
				new Expectations(){{
					//new StoreRemoteServices();
					storeService.loadDocument(anyString, anyString); result = originalDocument;
				}};
				
				ImportDocumentIntegrator service = new ImportDocumentIntegrator(jp, spread_id);
				service.execute();
				
				
				SpreadSheet result = bd.getSpreadsheetById(service.get_docId());
				
				
				
				assertTrue("Cell content doesnt match", sameSpreadSheet(result, spreadsheet));
				assertEquals("Cell content doesnt match", result.getOwner(), spreadsheet.getOwner());
				assertFalse("Cell content doesnt match", result.getId() == spreadsheet.getId());
				assertEquals("Cell content doesnt match", result.getReaderUserSet().size(), 1);
				assertEquals("Cell content doesnt match", result.getWriterUserSet().size(), 1);
				
				
			}
			
			@Test
			public void successTwice() {
				
				
				BubbleDocs bd = BubbleDocs.getInstance();

				
				
				
				
				new Expectations(){{
					//new StoreRemoteServices();
					storeService.loadDocument(anyString, anyString); result = originalDocument;
					
					//new StoreRemoteServices();
					storeService.loadDocument(anyString, anyString); result = originalDocument;
				}};
				
				ImportDocumentIntegrator service = new ImportDocumentIntegrator(jp, spread_id);
				service.execute();
				
				service = new ImportDocumentIntegrator(jp, spread_id);
				service.execute();
				
				
				SpreadSheet result = bd.getSpreadsheetById(service.get_docId());

				
				
				assertTrue("Cell content doesnt match", sameSpreadSheet(result, spreadsheet));
				assertEquals("Cell content doesnt match", result.getOwner(), spreadsheet.getOwner());
				assertFalse("Cell content doesnt match", result.getId() == spreadsheet.getId());
				assertEquals("Cell content doesnt match", result.getReaderUserSet().size(), 1);
				assertEquals("Cell content doesnt match", result.getWriterUserSet().size(), 1);
				
				
			}
			
			
			@Test(expected = CannotLoadDocumentException.class)

			public void userHasNoRights() {
				
				new Expectations(){{
					new StoreRemoteServices();
					storeService.loadDocument(anyString, anyString); result = new CannotLoadDocumentException();
				}};
				
				ImportDocumentIntegrator service = new ImportDocumentIntegrator(nwp, spread_id);
				service.execute();

			}
			
			@Test(expected = CannotLoadDocumentException.class)

			public void docWasntExported() {
				
				new Expectations(){{
					new StoreRemoteServices();
					storeService.loadDocument(anyString, anyString); result = new CannotLoadDocumentException();
				}};
				
				ImportDocumentIntegrator service = new ImportDocumentIntegrator(nwp, spread_id);
				service.execute();

			}
			

}
