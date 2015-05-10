package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;

public class RenewPassword extends LoggedBubbleDocsService {
	private String _token;
	
	public RenewPassword(String token) {
		this._token = token;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		validateUser(this._token);
		
	}
	
	public void setUserPassword(String username, String password) {
		
		BubbleDocs bd = BubbleDocs.getInstance();
		
		User u = bd.getUserByUsername(username);
		
		u.setPassword(password);
		
	}

}