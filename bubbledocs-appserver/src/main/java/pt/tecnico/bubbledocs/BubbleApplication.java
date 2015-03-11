package pt.tecnico.bubbledocs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;

public class BubbleApplication {
	//private static final Logger logger = LoggerFactory.getLogger(BubbleApplication.class);

	public static void main(String[] args){
		//test2();
		//test1();
		test1();
		
	}
	@Atomic
	public static void t(){
		BubbleDocs bd = BubbleDocs.getInstance();
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

}
