package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Session;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.service.LoginUser;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;


public class LoginUserIntegrator extends BubbleDocsIntegrator {
	
	private LoginUser service;
	private String username;
	private String password;
	private IDRemoteServices idService;
	
	public LoginUserIntegrator(String username, String password) {
		
		service = new LoginUser(username,password);
		this.username = username;
		this.password = password;
		
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException {
		
		idService = new IDRemoteServices();
		
		try {			
			//remote login
			idService.loginUser(username, password);
			
			service.setUserPassword(username, password);
			
		} catch (RemoteInvocationException e) {
			
			//local login
			if(service.checkUserPassword(username) == null)
				throw new UnavailableServiceException();
			
			if(!service.checkUserPassword(username).equals(password))
				throw new LoginBubbleDocsException();
		}		
		
		service.execute();
	}
	
	public final String getUserToken() {
		return service.getUserToken();
	}

}
