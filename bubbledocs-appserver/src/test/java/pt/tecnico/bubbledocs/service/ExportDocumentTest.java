package pt.tecnico.bubbledocs.service;

import org.joda.time.Hours;
import org.joda.time.LocalTime;
import org.joda.time.Seconds;
import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Argument;
import pt.tecnico.bubbledocs.domain.BinaryFunction;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Content;
import pt.tecnico.bubbledocs.domain.LiteralArgument;
import pt.tecnico.bubbledocs.domain.LiteralContent;
import pt.tecnico.bubbledocs.domain.ReferenceArgument;
import pt.tecnico.bubbledocs.domain.ReferenceContent;
import pt.tecnico.bubbledocs.domain.Session;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.SpreadSheetIdUnknown;
import pt.tecnico.bubbledocs.exception.UserHasNotReadAccessException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import static org.junit.Assert.*;

public class ExportDocumentTest extends BubbleDocsServiceTest {
	
	private String ruiToken; // the token for the user rui
	private User rui;
	private SpreadSheet s1;
	
	
	private static final String USERNAME = "rui";
	private static final String PASSWORD = "rui";
	private static final String NAME = "Rui";
	
	@Override
	public void populate4Test() {
		rui = createUser(USERNAME, PASSWORD, NAME);
		ruiToken = addUserToSession(USERNAME);
		s1 = rui.createSheet("Test", 3, 2);
		s1.getCell(1,1).setLiteralContent(2);
		s1.getCell(1,2).setReferenceContent(s1.getCell(1, 1));
		s1.getCell(2,1).setBFAdd(new LiteralArgument(3), new ReferenceArgument(s1.getCell(1, 1)));
		s1.getCell(2,2).setBFSub(new LiteralArgument(3), new ReferenceArgument(s1.getCell(1, 1)));
		s1.getCell(3,1).setBFMul(new LiteralArgument(3), new ReferenceArgument(s1.getCell(1, 1)));
		s1.getCell(3,2).setBFDiv(new LiteralArgument(3), new ReferenceArgument(s1.getCell(1, 1)));		
	}
	
	private boolean sameSpreadSheet(SpreadSheet s1,SpreadSheet s2){
		assertTrue(s1.getId()!=s2.getId());
		assertTrue(s1.getName().equals(s2.getName()));
		assertTrue(s1.getCreationDate().equals(s2.getCreationDate()));
		assertTrue(s1.getLines()==s2.getLines());
		assertTrue(s1.getColumns()==s2.getColumns());
		for(int i=1;i<=3;i++)
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

	@Test
    public void success() {
		ExportDocument serv = new ExportDocument(ruiToken, s1.getId());
		serv.execute();		
		BubbleDocs bd = BubbleDocs.getInstance();
		SpreadSheet s2 = bd.importSheet(serv.getDocXML(), USERNAME);
		assertTrue("The imported spreadSheet from the exported SpreadSheet are diferent", sameSpreadSheet(s1, s2));		
	}
	
	@Test
    public void successTwice() {
		ExportDocument serv = new ExportDocument(ruiToken, s1.getId());
		serv.execute();	
		BubbleDocs bd = BubbleDocs.getInstance();
		SpreadSheet s2 = bd.importSheet(serv.getDocXML(), USERNAME);
		serv.execute();
		SpreadSheet s3 = bd.importSheet(serv.getDocXML(), USERNAME);
		assertTrue("Original SpreadSheet is diferent from the one imported from XML", sameSpreadSheet(s1, s2));		
		assertTrue("Original SpreadSheet is diferent from the one imported from XML", sameSpreadSheet(s1, s3));	
		assertTrue("Original SpreadSheet is diferent from the one imported from XML", sameSpreadSheet(s2, s3));	
	}
	
	@Test(expected = SpreadSheetIdUnknown.class)
	public void exportUnknownId(){
		ExportDocument serv = new ExportDocument(ruiToken, 15);
		serv.execute();
	}
	
	@Test(expected = UserHasNotReadAccessException.class)
	public void noReadPermission(){
		createUser("botelho", "botelho", NAME);
		String token = addUserToSession("botelho");
		ExportDocument serv = new ExportDocument(token, s1.getId());
		serv.execute();				
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void invalidToken(){
		createUser("botelho", PASSWORD, NAME);
		ExportDocument serv = new ExportDocument("botelho-0", s1.getId());
		serv.execute();		
	}
	
	@Test
	public void renewToken(){
		BubbleDocs bd = BubbleDocs.getInstance();
		Session s = bd.getUserByToken(ruiToken).getSession();
		LocalTime start = s.getLastAccess();
		ExportDocument serv = new ExportDocument(ruiToken, s1.getId());
		serv.execute();
		LocalTime end = s.getLastAccess();
		int diference = end.getMillisOfSecond()-start.getMillisOfSecond();
		assertTrue("The session lease is not renewed", diference > 0);		
	}	
}
