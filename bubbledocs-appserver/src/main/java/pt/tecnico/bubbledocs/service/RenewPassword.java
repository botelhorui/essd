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
		BubbleDocs bd = BubbleDocs.getInstance();
		User user = bd.getUserByToken(this._token);
		
		validateUser(this._token);
		
		try {
			//remote renew password
			bd.IDRemoteServices.renewPassword(user.getUsername());
			user.setPassword(null);
			
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();
		}
		
	}

}