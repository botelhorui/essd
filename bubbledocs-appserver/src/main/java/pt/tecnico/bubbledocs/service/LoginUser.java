package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

// add needed import declarations

public class LoginUser extends BubbleDocsService {

	private String userToken;
	private String username;
	private String password;

	public LoginUser(String username, String password) {
		// TODO
		this.username=username;
		this.password=password;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		// add code here
	}

	public final String getUserToken() {
		return userToken;
	}
}
