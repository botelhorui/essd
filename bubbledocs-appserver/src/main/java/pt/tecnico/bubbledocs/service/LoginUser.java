package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Session;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.WrongPasswordException;

// add needed import declarations

public class LoginUser extends BubbleDocsService {

	private String token;
	private String username;
	private String password;

	public LoginUser(String username, String password) {
		this.username=username;
		this.password=password;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = BubbleDocs.getInstance();
		User u = bd.getUserByUsername(username);
		if(!u.getPassword().equals(password)){
			throw new WrongPasswordException();
		}
		Session s = u.getSession();
		if(s==null){
			s=new Session();
			u.setSession(s);
		}
		s.renewLassAccess();
		s.renewToken();
		token=s.getToken();
		bd.cleanInvalidSessions();
	}

	public final String getUserToken() {
		return token;
	}
}
