package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;

// add needed import declarations

public class CreateUser extends LoggedBubbleDocsService {
	private String token;
	private String username;
	private String email;
	private String name;
	
	public CreateUser(String token, String username, String name, String email) {
		this.username = username;
		this.email = email;
		this.name = name;
		this.token = token;
	}
	
	@Override
	protected void validateUser(String token) throws BubbleDocsException{
    	
		super.validateUser(token);
    	
    	BubbleDocs bd = BubbleDocs.getInstance();
    	bd.checkIfRoot(token);	
    
	}
	
	protected void checkEmail(String email) throws BubbleDocsException {
    	
    	BubbleDocs bd = BubbleDocs.getInstance();
    	bd.checkEmail(email);	
    
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		BubbleDocs bd = BubbleDocs.getInstance();
		User user = bd.getUserByToken(this.token);
		
		if(username.equals(""))
			throw new EmptyUsernameException();
		
		validateUser(this.token);
		checkEmail(this.email);
		
		try {
			
			bd.IDRemoteServices.createUser(this.username, this.name, this.email);
			
		} catch (RemoteInvocationException e) {
			
			throw new UnavailableServiceException();
			
		}
		
		bd.createUser(this.username, this.name, this.email);
		
	}
}
