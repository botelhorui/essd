package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Session;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.service.LoginUser;


public class LoginUserIntegrator extends BubbleDocsIntegrator {
	
	private LoginUser service;
	private String username;
	private String password;
	
	public LoginUserIntegrator(String username, String password) {
		
		service = new LoginUser(username,password);
		this.username = username;
		this.password = password;
		
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException {
		
		BubbleDocs bd = BubbleDocs.getInstance();
		User u = bd.getUserByUsername(username);
		
		try {
			//remote login
			bd.IDRemoteServices.loginUser(username, password);
			// save copy
			u.setPassword(password);
		} catch (RemoteInvocationException e) {
			//local login
			if(u.getPassword()==null)
				throw new UnavailableServiceException();
			
			if(!u.getPassword().equals(password)){
				throw new LoginBubbleDocsException();
			}
		}		
		
		service.execute();
	}	

}
