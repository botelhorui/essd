package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.CharacterLimitException;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;

// add needed import declarations

public class CreateUser extends RootBubbleDocsService {
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

	public String getUsername() {

		return username;
	}

	public String getName() {

		return name;
	}

	public String getEmail() {

		return email;
	}

	protected void validateFields(String username) throws BubbleDocsException {

		BubbleDocs bd = BubbleDocs.getInstance();

		if((username.length() < 3) || (username.length() > 8)){
			throw new CharacterLimitException();
		}

	}

	@Override
	protected void dispatch() throws BubbleDocsException {

		BubbleDocs bd = BubbleDocs.getInstance();

		// FIX ME 
		validateUser(this.token);
		bd.validateUsername(this.username);

		bd.createUser(this.username, this.name, this.email);

	}
}
