package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UnknownBubbleDocsUserException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;

// add needed import declarations

public class DeleteUser extends LoggedBubbleDocsService {
	private String token;
	private String username;
	
	public DeleteUser(String token, String toDeleteUsername) {
		
		this.username = toDeleteUsername; 
		this.token = token;
	}
	
	@Override
	protected void validateUser(String token) throws BubbleDocsException{
    	super.validateUser(token);
    	
    	BubbleDocs bd = BubbleDocs.getInstance();
    	bd.checkIfRoot(token);	
    }

	@Override
	protected void dispatch() throws BubbleDocsException, UnavailableServiceException {
		
		IDRemoteServices service = new IDRemoteServices();
		BubbleDocs bd = BubbleDocs.getInstance();
		User user = bd.getUserByToken(this.token);
		User userToDelete = bd.getUserByUsername(this.username);
		
		validateUser(this.token);
		
		/*
		 * The following code has not been moved nor removed because
		 * I'm not exactly sure how to proceed. An e-mail has been sent about
		 * this issue, and I'll leave this here until I get a response.
		 */
		
		if(userToDelete == null)
			throw new UnknownBubbleDocsUserException();
		
		if(user == null)
			throw new UnknownBubbleDocsUserException();
		
		userToDelete.delete();
		
		try {
			
			service.removeUser(user.getName());
		} catch (Exception e) {
			
			throw new UnavailableServiceException();
		}
		
	}

}
