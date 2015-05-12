package pt.tecnico.bubbledocs;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;

// add needed import declarations

public class BubbleDocsTest {

	// auxiliary methods that access the domain layer and are needed in the test classes
	// for defining the initial state and checking that the service has the expected behavior
	public User createUser(String username, String name, String email) {
		BubbleDocs bd = BubbleDocs.getInstance();
		return bd.createUser(username, name, email);  
	}

	public SpreadSheet createSpreadSheet(User user, String name, int lines, int columns) {
		return user.createSheet(name, lines, columns);
	}

	// returns a spreadsheet whose name is equal to name
	public SpreadSheet getSpreadSheet(String name) {
		BubbleDocs bd = BubbleDocs.getInstance();
		SpreadSheet n = null;
		for(SpreadSheet x: bd.getSpreadSheetSet()){
			if(x.getName().equals(name)) {
				n = x;
				break;
			}
		}
		
		return n;
	}
	
	public SpreadSheet getSpreadSheetById(int id) {
		BubbleDocs bd = BubbleDocs.getInstance();
		for(SpreadSheet s: bd.getSpreadSheetSet()){
			if(s.getId()==id)
				return s;
		}
		return null;
	}

	// returns the user registered in the application whose username is equal to username
	public User getUserFromUsername(String username) {
		BubbleDocs bd = BubbleDocs.getInstance();
		return bd.getUserByUsername(username);
	}

	// put a user into session and returns the token associated to it
	// Deprecated -- stays here for reference until the project is reviewed in its entirety.
/*	public String addUserToSession(String username) {
		BubbleDocs bd = BubbleDocs.getInstance();
		User u = bd.getUserByUsername(username);
		String password = u.getPassword();
		LoginUser service = new LoginUser(username, password);
		service.execute();
		return service.getUserToken();
	}*/
	
	
	public String addUserToSession(String username) {
		
		BubbleDocs bd = BubbleDocs.getInstance();
		User u = bd.getUserByUsername(username);
		bd.renewSessionDuration(u);
		bd.renewToken(u);
		return u.getSession().getToken();
		
	}
	

	// remove a user from session given its token
	public void removeUserFromSession(String token) {
		BubbleDocs bd = BubbleDocs.getInstance();
		User u = bd.getUserByToken(token);
		u.getSession().delete();
	}

	// return the user registered in session whose token is equal to token
	public User getUserFromSession(String token) {
		BubbleDocs bd = BubbleDocs.getInstance();
		return bd.getUserByToken(token);
	}
}
