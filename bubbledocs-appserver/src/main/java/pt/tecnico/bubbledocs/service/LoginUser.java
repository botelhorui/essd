package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Session;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;

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
		if(u==null){
			throw new LoginBubbleDocsException();
		}
		bd.cleanInvalidSessions();
		try {
			//remote login
			bd.IDRemoteServices.loginUser(username, password);
			// save copy
			u.setPassword(password);
		} catch (RemoteInvocationException e) {
			//local login
			if(u.getPassword()==null)
				throw new UnavailableServiceException();
			
			if(!u.getPassword().equals(password)){
				throw new LoginBubbleDocsException();
			}
		}		
		bd.renewSessionDuration(u);
		bd.renewToken(u);
		token=u.getSession().getToken();
	}

	public final String getUserToken() {
		return token;
	}
}
