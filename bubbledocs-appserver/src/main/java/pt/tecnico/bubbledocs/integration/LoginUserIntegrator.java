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
		
		//BubbleDocs bd = BubbleDocs.getInstance();
		
		//NAO SE PODE USAR OBJETOS DO DOMINIO!!
		//User u = bd.getUserByUsername(username);
		
		try {
			//remote login
			idService.loginUser(username, password);
			// save copy
			// NAO SE PODE USAR OBJETOS DO DOMINIO!
			//u.setPassword(password);
		} catch (RemoteInvocationException e) {
			
			//NAO SE PODE USAR OBJETOS DO DOMINIO!!
			//local login
			//if(u.getPassword()==null)
				//throw new UnavailableServiceException();
			
			//if(!u.getPassword().equals(password)){
				//throw new LoginBubbleDocsException();
			//}
		}		
		
		service.execute();
	}
	
	public final String getUserToken() {
		return service.getUserToken();
	}

}
