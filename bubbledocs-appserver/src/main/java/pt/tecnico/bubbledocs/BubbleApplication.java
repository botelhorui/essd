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
import pt.tecnico.bubbledocs.domain.SheetAccess;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.domain.LiteralArgument;
import pt.tecnico.bubbledocs.domain.ReferenceArgument;

import pt.tecnico.bubbledocs.exception.UserIsNotOwnerException;
import pt.tecnico.bubbledocs.exception.UserDoesNotExistException;
import pt.tecnico.bubbledocs.exception.PositionOutOfBoundsException;

@SuppressWarnings("unused")
public class BubbleApplication {
	public static void main(String[] args){
		System.out.println("Welcome to the BubbleDocs application!");	
		TransactionManager tm = FenixFramework.getTransactionManager();
		boolean committed = false;
		try {
			tm.begin();
			//
			populateDomain();
			//
			printUsers(); 
			//
			printAllUserSheets("pf"); 
			printAllUserSheets("ra"); 
			//
			Document doc = exportSheet(BubbleDocs.getInstance().getUserByUsername("pf").getOwnedSpreadByName("Notas ES").get(0));
			//
			//deleteSheet("pf","Notas ES");
			//
			//printAllUserSheets("pf"); 
			//
			//importSheet(doc);
			//
			//printAllUserSheets("pf"); 
			//
			//exportPfSheet();
			//
			System.out.println("Finished ");			
			tm.commit();
			committed = true;
			
		} catch (SystemException|
				NotSupportedException |
				RollbackException|
				HeuristicMixedException |
				HeuristicRollbackException ex) {
			System.err.println("Error in execution of transaction: ");
			ex.printStackTrace();
		
		} catch (UserDoesNotExistException e) {
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

	/*@Atomic
	private static void importSheet(Document doc) {
	
		System.out.println("Importing 1 sheet. \"Notas ES\"");
		BubbleDocs.getInstance().importSheet(doc, "pf");
	
	}*/

	@Atomic
	private static void deleteSheet(String username, String sheetname) {
		
		try {
			
			User user = BubbleDocs.getInstance().getUserByUsername(username);
			
			System.out.println("Deleting user \"" + username + "\"'s sheet \""+ sheetname +"\"");
			SpreadSheet sheet;
		
			sheet = user.getOwnedSpreadByName(sheetname).get(0);
			sheet.delete();
			sheet = null;
		}
		catch (UserDoesNotExistException e) {
			System.out.println("User \"" + username + "\" does not exist.");
		}
		catch (UserIsNotOwnerException e) {
			System.out.println("User \"" + username + "\" has not created sheet \"" + sheetname + "\".");
		}
	}

	/*
	 * Deprecated
	 * 
	@Atomic
	private static Document exportPfSheet() {
		User pf = BubbleDocs.getInstance().getUserByUsername("pf");
		System.out.println("Exporting "+ pf.getSpreadSheetSet().size()+" sheets:");			
		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		Document doc=null;
		for(SpreadSheet x: pf.getSpreadSheetSet()){
			doc = x.export();				
			System.out.println(xml.outputString(doc));
			break;
		}
		return doc;
	}
	*/
	
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

	@Atomic
	public static boolean isInitialized(){
		BubbleDocs bd = BubbleDocs.getInstance();
		return !bd.getUserSet().isEmpty();
	}

	@Atomic
	private static void populateDomain() {
		if(isInitialized())
			return;		
		BubbleDocs bd = BubbleDocs.getInstance();
		User pf = bd.createUser("pf","sub","Paul Door");
		bd.createUser("ra","cor","Step Rabbit");
		SpreadSheet s1 = pf.createSheet("Notas ES",300,20);
		
		//TODO: Verificar permissoes do user para read/write
		try{
			s1.getCell(3, 4).setLiteralContent(5);
			
			s1.getCell(1,1).setReferenceContent(s1.getCell(5,6));
			
			s1.getCell(5,6).setBFAdd(new LiteralArgument(2), new ReferenceArgument(s1.getCell(3,4)));
			
			s1.getCell(2,2).setBFDiv(new ReferenceArgument(s1.getCell(1,1)), new ReferenceArgument(s1.getCell(3,4)));
			
		}catch (PositionOutOfBoundsException e){
			System.out.println();
		}
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
		catch (UserDoesNotExistException e) {
			System.out.println("User \"" + username + "\" does not exist.");
		}
	}
}
