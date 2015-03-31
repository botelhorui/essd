package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;

// add needed import declarations

public class CreateUser extends BubbleDocsService {
	private String token;
	private String newUsername;
	private String password;
	private String name;
	
	public CreateUser(String token, String newUsername,String password, String name) {
		this.newUsername = newUsername;
		this.password = password;
		this.name = name;
		this.token = token;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		BubbleDocs bd = BubbleDocs.getInstance();
		User user = bd.getUserByToken(this.token);
		
		
		if(newUsername.equals(""))
			throw new EmptyUsernameException();
		
		if(!(bd.isUserInSession(this.token)))
			throw new UserNotInSessionException();
		
		if(!(user.getUsername().equals("root")))
			throw new UnauthorizedOperationException();
		
		bd.createUser(this.newUsername, this.password, this.name);
		
	}
}
