package pt.tecnico.bubbledocs;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Sheet;
import pt.tecnico.bubbledocs.domain.SheetAccess;
import pt.tecnico.bubbledocs.domain.User;

public class BubbleApplication {
	//private static final Logger logger = LoggerFactory.getLogger(BubbleApplication.class);

	public static void main(String[] args){
		System.out.println("Welcome to the BubbleDocs application!");

		TransactionManager tm = FenixFramework.getTransactionManager();
		boolean committed = false;
		try {
			tm.begin();

			test6();	
			
			tm.commit();
			committed = true;
		}catch (SystemException| NotSupportedException | RollbackException| HeuristicMixedException | HeuristicRollbackException ex) {
			System.err.println("Error in execution of transaction: " + ex);
		} finally {
			if (!committed) 
				try {
					tm.rollback();
				} catch (SystemException ex) {
					System.err.println("Error in roll back of transaction: " + ex);
				}
		}		
	}

	@Atomic
	public static void test1(){
		BubbleDocs bd = BubbleDocs.getInstance();
		User rui = bd.getUserByUsername("rui");
		if(rui==null){
			System.out.println("There is no user rui.");
			rui = new User();
			rui.init("rui","rui","lol");
			bd.addUser(rui);
			System.out.println("Created rui and added it to bd");
		}

		rui.delete();
		System.out.println("Deleted user rui. Has user rui: "+bd.hasUser("rui"));
	}
	@Atomic
	public static void test2(){
		TransactionManager tm = FenixFramework.getTransactionManager();
		try {
			tm.begin();
			BubbleDocs bd = BubbleDocs.getInstance();
			System.out.println("Is there user rui? "+ bd.hasUser("rui"));
			User rui = new User();
			rui.init("rui", "rui", "rui");
			SheetAccess s = rui.createSheet("notas", 10, 10);
			System.out.println("created sheet at: "+s.getSheetData().getCreationDate());
			
			
		} catch (Exception e) {
			System.out.println("Error during transaction"+e);
		} finally{
			try {
				tm.rollback();
			} catch (SystemException e) {
				System.out.println("Error in rollback:"+e);
				e.printStackTrace();
			}
		}
		
	}
	@Atomic
	public static void test3(){
		BubbleDocs bd = BubbleDocs.getInstance();
		User pf = new User();
		pf.init("pf","sub","Paul Door");
		bd.addUser(pf);
		User ra = new User();
		ra.init("ra","cor","Step Rabbit");
		bd.addUser(ra);
		Sheet s1 = pf.createSheet("Notas ES",300,20);
		s1.setCell(3,4,"5");
		s1.setCell(1,1,"5;6");
		s1.setCell(5,6,"=ADD(2,3;4");
		s1.setCell(2,2,"=DIV(1;1,3;4)");
	}
	
	@Atomic
	public static void test4(){	
		BubbleDocs bd = BubbleDocs.getInstance();
		User pf = bd.getUserByUsername("pf");
		Document doc = pf.getSheetAccessByName("Notas ES").export();
		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		System.out.println(xml.outputString(doc));
		
	}
	
	@Atomic
	public static void test5(){
		BubbleDocs bd = BubbleDocs.getInstance();
		User pf = bd.getUserByUsername("pf");
		pf.getSheetAccessByName("Notas ES").delete();
	}
	
	@Atomic
	public static void test6(){
		BubbleDocs bd = BubbleDocs.getInstance();
		User pf = new User();
		pf.init("pf","sub","Paul Door");
		bd.addUser(pf);
		Sheet s1 = pf.createSheet("Notas ES",300,20);
		s1.setCell(3,4,"5");
		s1.setCell(1,1,"5;6");
		s1.setCell(5,6,"=ADD(2,3;4)");
		s1.setCell(2,2,"=DIV(1;1,3;4)");
		Document doc = s1.export();
		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		System.out.println(xml.outputString(doc));
		bd.importSheet(doc,"pf");
	}
	


}
