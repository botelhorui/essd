package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;

// add needed import declarations

public class DeleteUser extends RootBubbleDocsService {
	private String token;
	private String username;
	
	public DeleteUser(String token, String toDeleteUsername) {
		
		this.username = toDeleteUsername; 
		this.token = token;
	}

	@Override
	protected void dispatch() throws BubbleDocsException, UnavailableServiceException {
		
		
		// NOTE: NOT SURE IF VALIDATIONS ARE REDUNDANT
		
		BubbleDocs bd = BubbleDocs.getInstance();
		validateUser(this.token);
		User user = bd.getUserByToken(this.token);
		User userToDelete = bd.getUserByUsername(this.username);
		
		if(userToDelete == null)
			throw new LoginBubbleDocsException();
		
		if(user == null)
			throw new LoginBubbleDocsException();
		
		userToDelete.delete();
		
	}

}
