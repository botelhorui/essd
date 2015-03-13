package pt.tecnico.bubbledocs;

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

			test();	
			
			tm.commit();
			committed = true;
		}catch (SystemException|
				NotSupportedException |
				RollbackException|
				HeuristicMixedException |
				HeuristicRollbackException ex) {
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
	public static void test6(){
		BubbleDocs bd = BubbleDocs.getInstance();
		User pf = new User();
		pf.init("pf","sub","Paul Door");
		bd.addUser(pf);
		SheetData s1 = pf.createSheet("Notas ES",300,20);
		s1.setCellText("pf",3,4,"5");
		s1.setCellText("pf",1,1,"5;6");
		s1.setCellText("pf",5,6,"=ADD(2,3;4)");
		s1.setCellText("pf",2,2,"=DIV(1;1,3;4)");
		Document doc = s1.export();
		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		System.out.println(xml.outputString(doc));
		bd.importSheet(doc,"pf");
	}
	
	@Atomic
	public static void test(){
		BubbleDocs bd = BubbleDocs.getInstance();
		bd.getUserByUsername("pf").delete();
	}


}
