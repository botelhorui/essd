package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

// add needed import declarations

public class CreateUser extends BubbleDocsService {
	private String token;
	public CreateUser(String token, String newUsername,String password, String name) {
		// add code here
		this.token=token;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		// add code here
	}
}
