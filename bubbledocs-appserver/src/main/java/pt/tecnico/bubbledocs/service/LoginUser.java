package pt.tecnico.bubbledocs.service;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;

// add needed import declarations

public class LoginUser extends BubbleDocsService {

	private String token;
	private String username;
	//private String password;

	public LoginUser(String username, String password) {
		this.username=username;
		//this.password=password;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = BubbleDocs.getInstance();
		User u = bd.getUserByUsername(username);
		if(u==null){
			throw new LoginBubbleDocsException();
		}

		bd.cleanInvalidSessions();

		bd.renewSessionDuration(u);
		bd.renewToken(u);
		token=u.getSession().getToken();
	}

	public final String getUserToken() {
		return token;
	}

	@Atomic
	public void setUserPassword(String username, String password) throws InvalidUsernameException{

		BubbleDocs bd = BubbleDocs.getInstance();

		User u = bd.getUserByUsername(username);

		u.setPassword(password);

	}

	@Atomic
	public String checkUserPassword(String username) throws InvalidUsernameException {

		BubbleDocs bd = BubbleDocs.getInstance();

		User u = bd.getUserByUsername(username);

		String password; 

		password = u.getPassword();

		return password;

	}
}
