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
import pt.tecnico.bubbledocs.domain.SheetData;
import pt.tecnico.bubbledocs.domain.User;

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
			printUsersSheets();
			//
			Document doc = exportPfSheet();
			
			deleteSheet();
			//
			printUsersSheets();
			//
			importSheet(doc);
			//
			printUsersSheets();
			//
			exportPfSheet();
			//
			System.out.println("Finished ");			
			tm.commit();
			committed = true;
		}catch (SystemException|
				NotSupportedException |
				RollbackException|
				HeuristicMixedException |
				HeuristicRollbackException ex) {
			System.err.println("Error in execution of transaction: ");
			ex.printStackTrace();
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
	private static void importSheet(Document doc) {
		System.out.println("Importing 1 sheet. \"Notas ES\"");
		BubbleDocs.getInstance().importSheet(doc, "pf");
	}

	@Atomic
	private static void deleteSheet() {
		User pf = BubbleDocs.getInstance().getUserByUsername("pf");
		System.out.println("Deleting sheet \"Notas ES\"");
		SheetData sd = pf.getSheetDataByName("Notas ES").get(0);
		sd.delete();
		sd=null;
	}

	@Atomic
	private static Document exportPfSheet() {
		User pf = BubbleDocs.getInstance().getUserByUsername("pf");
		System.out.println("Exporting "+ pf.getSheetDataSet().size()+" sheets:");			
		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		Document doc=null;
		for(SheetData x: pf.getSheetDataSet()){
			doc = x.export();				
			System.out.println(xml.outputString(doc));
			break;
		}
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
		SheetData s1 = pf.createSheet("Notas ES",300,20);
		s1.setCellText("pf",3,4,"5");
		s1.setCellText("pf",1,1,"5;6");
		s1.setCellText("pf",5,6,"=ADD(2,3;4)");
		s1.setCellText("pf",2,2,"=DIV(1;1,3;4)");			
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
			System.out.println("\tusername:"+u.getUsername()+" has "+u.getSheetDataSet().size()+" sheets:");
				for(SheetData x: u.getSheetDataSet()){
					System.out.println("\t\tSheet, name:\""+x.getName()+"\" id:"+x.getId());
				}
		}
	}


}
