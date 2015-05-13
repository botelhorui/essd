package pt.tecnico.bubbledocs.integration.component;

import mockit.Expectations;
import mockit.Mocked;

import org.joda.time.DateTime;
import org.junit.Test;

import pt.tecnico.bubbledocs.integration.ExportDocumentIntegrator;
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
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.SpreadSheetIdUnknown;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserHasNotReadAccessException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.exception.CannotStoreDocumentException;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;
import static org.junit.Assert.*;

public class ExportDocumentIntegratorTest extends BubbleDocsServiceTest {
	
	private String ruiToken; // the token for the user rui
	private String iurToken; // the token for the user iur
	private User rui;
	private User iur;
	private SpreadSheet s1;
	
	
	private static final String USERNAME = "rui";
	private static final String EMAIL = "rui@ist.utl.pt";
	private static final String NAME = "Rui";
	
	private static final String USERNAME2 = "iur";
	private static final String EMAIL2 = "iur@pt.utl.ist";
	private static final String NAME2 = "Iur";
	
	@Override
	public void populate4Test() {
		rui = createUser(USERNAME, NAME, EMAIL);
		iur = createUser(USERNAME2, NAME2, EMAIL2);
		
		ruiToken = addUserToSession(USERNAME);
		iurToken = addUserToSession(USERNAME2);
		s1 = rui.createSheet("Test", 3, 2);
		s1.getCell(1,1).setLiteralContent(2);
		s1.getCell(1,2).setReferenceContent(s1.getCell(1, 1));
		s1.getCell(2,1).setBFAdd(new LiteralArgument(3), new ReferenceArgument(s1.getCell(1, 1)));
		s1.getCell(2,2).setBFSub(new LiteralArgument(3), new ReferenceArgument(s1.getCell(1, 1)));
		s1.getCell(3,1).setBFMul(new LiteralArgument(3), new ReferenceArgument(s1.getCell(1, 1)));
		s1.getCell(3,2).setBFDiv(new LiteralArgument(3), new ReferenceArgument(s1.getCell(1, 1)));
		s1.addReader(iur);
		s1.addWriter(iur);
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
	
	@Test(expected = UnavailableServiceException.class)
	public void remoteServiceFault(@Mocked final StoreRemoteServices service){
		
		new Expectations(){{
			byte[] document=null;
			service.storeDocument(anyString, anyString, document); result = new RemoteInvocationException();
		}};
		
		ExportDocumentIntegrator serv = new ExportDocumentIntegrator(ruiToken, s1.getId());
		serv.execute();		
	}
	
	@Test(expected = CannotStoreDocumentException.class)
	public void CannotStoreDocument(@Mocked final StoreRemoteServices service){
		
		ExportDocumentIntegrator serv = new ExportDocumentIntegrator(ruiToken, s1.getId());
		
		new Expectations(){{
			byte[] document=null;
			service.storeDocument(anyString, anyString, document); 
			result = new CannotStoreDocumentException();
		}};
		
		serv.execute();

	}
	
}
