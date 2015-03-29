package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

// add needed import declarations

public class DeleteUser extends BubbleDocsService {
	private String token;
	public DeleteUser(String token, String toDeleteUsername) {
		// add code here
		this.token=token;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		// add code here
	}

}
