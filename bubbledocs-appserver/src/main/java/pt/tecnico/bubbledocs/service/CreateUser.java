package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.domain.BubbleDocs;

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

	@Override
	protected void dispatch() throws BubbleDocsException {

		BubbleDocs bd = BubbleDocs.getInstance();

		// FIX ME 
		validateUser(this.token);
		bd.validateUsername(this.username);

		bd.createUser(this.username, this.name, this.email);

	}
}
