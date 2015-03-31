package pt.tecnico.bubbledocs;

import java.util.ArrayList;
import java.util.List;
 







import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.domain.LiteralArgument;
import pt.tecnico.bubbledocs.domain.ReferenceArgument;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UserIsNotOwnerException;
import pt.tecnico.bubbledocs.exception.UnknownBubbleDocsUserException;
import pt.tecnico.bubbledocs.exception.PositionOutOfBoundsException;
import pt.tecnico.bubbledocs.service.AssignLiteralCell;
import pt.tecnico.bubbledocs.service.AssignReferenceCell;
import pt.tecnico.bubbledocs.service.CreateSpreadSheet;
import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.service.ExportDocument;
import pt.tecnico.bubbledocs.service.LoginUser;

@SuppressWarnings("unused")
public class BubbleApplication {
	public static void main(String[] args){
		System.out.println("Welcome to the BubbleDocs application!");	
		TransactionManager tm = FenixFramework.getTransactionManager();
		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		Document doc = null;
		boolean committed = false;
		try {
			//
			tm.begin();
			populateDomain();
			tm.commit();
			//
			tm.begin();
			printUsers();
			tm.commit();
			//
			tm.begin();
			printAllUserSheets("pf"); 
			printAllUserSheets("ra");
			tm.commit();
			//
			tm.begin();
			try{
				LoginUser pfLogin = new LoginUser("pf", "sub");
				pfLogin.execute();
				int id = BubbleDocs.getInstance().getUserByUsername("pf").getOwnedSpreadSheetsByName("Notas ES").get(0).getId();
				ExportDocument exportDoc = new ExportDocument(pfLogin.getUserToken(), id);
				exportDoc.execute();
				doc = exportDoc.getDocXML();
				System.out.println(xml.outputString(doc));
			} catch (BubbleDocsException e){
				
				System.out.println("Falha ao exportar o Documento \"Notas ES\""+e.getClass().getName());
				
			}
			
			
			
			//Document doc = exportSheet(BubbleDocs.getInstance().getUserByUsername("pf").getOwnedSpreadSheetsByName("Notas ES").get(0));
			tm.commit();
			//
			tm.begin();
			deleteSheet("pf","Notas ES");
			tm.commit();
			//
			tm.begin();
			printAllUserSheets("pf"); 
			tm.commit();
			//
			tm.begin();
			importSheet(doc, "pf");
			tm.commit();
			//
			tm.begin();
			printAllUserSheets("pf");
			tm.commit();
			//
			tm.begin();
			
			try{
				LoginUser pfLogin = new LoginUser("pf", "sub");
				pfLogin.execute();
				int id = BubbleDocs.getInstance().getUserByUsername("pf").getOwnedSpreadSheetsByName("Notas ES").get(0).getId();
				ExportDocument exportDoc = new ExportDocument(pfLogin.getUserToken(), id);
				exportDoc.execute();
				doc = exportDoc.getDocXML();
				System.out.println(xml.outputString(doc));
			} catch (BubbleDocsException e){
				
				System.out.println("Falha ao exportar o Documento \"Notas ES\""+e.getClass().getName());
				
			}
			
			
			
			//exportSheet(BubbleDocs.getInstance().getUserByUsername("pf").getOwnedSpreadSheetsByName("Notas ES").get(0));					
			tm.commit();
			System.out.println("Finished.");	
			committed = true;
			
		} catch (SystemException|
				NotSupportedException |
				RollbackException|
				HeuristicMixedException |
				HeuristicRollbackException ex) {
			System.err.println("Error in execution of transaction: " + ex);
			ex.printStackTrace();
		
		} catch (UnknownBubbleDocsUserException e) {
			System.out.println("User pf does not exist.");
		} finally {
		
			if (!committed){
				System.out.println("Rolling back");
				try {
					tm.rollback();
				} catch (SystemException ex) {
					System.err.println("Error in roll back of transaction: " + ex);
				}
			}
		}		
	}

	@Atomic
	private static void importSheet(Document doc, String username){
		System.out.println("Importing SpreadSheet from XML");		
		BubbleDocs.getInstance().importSheet(doc, username);		
	}

	@Atomic
	private static void deleteSheet(String username, String sheetname) {
		
		try {
			
			User user = BubbleDocs.getInstance().getUserByUsername(username);
			
			System.out.println("Deleting user \"" + username + "\"'s sheet \""+ sheetname +"\"");
			SpreadSheet sheet;			
			sheet = user.getOwnedSpreadSheetsByName(sheetname).get(0);
			sheet.delete();
			sheet = null;
		}
		catch (UnknownBubbleDocsUserException e) {
			System.out.println("User \"" + username + "\" does not exist.");
		}		
	}

	@Atomic
	private static Document exportSheet(SpreadSheet ss){
		System.out.println("Exporting Spreadsheet " + ss.getName() + " with " + ss.getLines() + " lines and " + ss.getColumns() + " columns.");
		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		Document doc = null;
		doc = ss.export();
		System.out.println(xml.outputString(doc));
		return doc;
	}

	
	/*
	 * Not using this version yet.
	 */
	/*@Atomic
	public static boolean isInitialized(){
		BubbleDocs bd = BubbleDocs.getInstance();
		return !bd.getUserSet().isEmpty();
	}*/

	@Atomic
	private static void populateDomain() {
		BubbleDocs bd = BubbleDocs.getInstance();
		User root = bd.getUserByUsername("root");
		LoginUser rootLogin = new LoginUser("root", "root");
		rootLogin.execute();
		
		
		
		
		//==========================================
		//codigo com servicos
		
		if(!bd.hasUser("ra")){
			System.out.println("Do I come in here?");
			try{
				
				CreateUser serviceUser = new CreateUser(rootLogin.getUserToken(), "ra", "cor", "Step Rabbit");
				serviceUser.execute();
			
			} catch (BubbleDocsException e){
				System.out.println("Falha ao criar o user \'ra\' "+e.getClass().getName());
			}
		}
		
		
	
		
		
		if(!bd.hasUser("pf")){
			System.out.println("What about here?");
			try{
				
				CreateUser serviceUser = new CreateUser(rootLogin.getUserToken(), "pf", "sub", "Paul Door");
				serviceUser.execute();
			
			} catch (BubbleDocsException e){
				System.out.println("Failed creation of user \'pf\' "+e.getClass().getName());
			}
		
		
		
		
			try{
				User pf = bd.getUserByUsername("pf");
				LoginUser pfLogin = new LoginUser("pf", "sub");
				pfLogin.execute();
				
				CreateSpreadSheet serviceSheet = new CreateSpreadSheet(pfLogin.getUserToken(), "Notas ES", 300, 20);
				serviceSheet.execute();
				
				AssignLiteralCell serviceLiteralCell = new AssignLiteralCell(pfLogin.getUserToken(), serviceSheet.getSheetId(), "3;4", "5");
				serviceLiteralCell.execute();
				
				AssignReferenceCell serviceReferenceCell = new AssignReferenceCell(pfLogin.getUserToken(), serviceSheet.getSheetId(), "1;1", "5;6");
				serviceReferenceCell.execute();
				
				SpreadSheet s1 = serviceSheet.getSpreadSheet();
				
				s1.getCell(5,6).setBFAdd(new LiteralArgument(2), new ReferenceArgument(s1.getCell(3,4)));
				
				s1.getCell(2,2).setBFDiv(new ReferenceArgument(s1.getCell(1,1)), new ReferenceArgument(s1.getCell(3,4)));
				
				
			} catch (BubbleDocsException e){
				System.out.println("Failed creation of SpreadSheet by user \'pf\' "+e.getClass().getName());
			}
		}
		
		//===========================================
		//codigo antigo
		/*
		if(!bd.hasUser("pf")){
			User pf = bd.createUser("pf","sub","Paul Door");	
			SpreadSheet s1 = pf.createSheet("Notas ES", 300, 20);	
			//TODO: Verificar permissoes do user para read/write
			try{
				s1.getCell(3, 4).setLiteralContent(5);
				
				s1.getCell(1,1).setReferenceContent(s1.getCell(5,6));
				
				s1.getCell(5,6).setBFAdd(new LiteralArgument(2), new ReferenceArgument(s1.getCell(3,4)));
				
				s1.getCell(2,2).setBFDiv(new ReferenceArgument(s1.getCell(1,1)), new ReferenceArgument(s1.getCell(3,4)));
				
			}catch (PositionOutOfBoundsException e){
				System.out.println("Trying to access an out of bounds cell.");
			}
		}
		
		//=============================================
		
		 */
		
		
	}

	@Atomic
	private static void printUsers() {
		System.out.println("Registered users are:");
		for(User u: BubbleDocs.getInstance().getUserSet()){
			System.out.println("\tusername:"+u.getUsername()+" name:"+u.getName()+" password:"+u.getPassword());
		}
	}

	@Atomic
	private static void printUsersSheets() {
		
		System.out.println("Registered users and their sheets names:");
		 
		for(User u: BubbleDocs.getInstance().getUserSet()){
			System.out.println("\tusername:"+u.getUsername()+" has "+u.getOwnedSpreadSet().size()+" sheets:");
			for(SpreadSheet x: u.getOwnedSpreadSet()){
				System.out.println("\t\tSheet, name:\""+x.getName()+"\" id:"+x.getId());
			}
		}
	}

	@Atomic
	private static void printAllUserSheets(String username) {

		try {
			User u = BubbleDocs.getInstance().getUserByUsername(username);

			System.out.println(username + "'s Sheets (" + u.getOwnedSpreadSet().size() + " sheets found):");
			
			for(SpreadSheet x: u.getOwnedSpreadSet()){
				System.out.println("\tSheet, name:\""+x.getName()+"\" id:"+x.getId());
			}
		}
		catch (UnknownBubbleDocsUserException e) {
			System.out.println("User \"" + username + "\" does not exist.");
		}
	}
}