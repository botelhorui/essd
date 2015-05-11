package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.service.RenewPassword;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.service.GetUsername4TokenService;

public class RenewPasswordIntegrator extends BubbleDocsIntegrator{
	
	private RenewPassword service;
	private String _token;
	private IDRemoteServices idService;
	private GetUsername4TokenService usernameService;
	
	public RenewPasswordIntegrator(String token) {
		
		service = new RenewPassword(token);
		this._token = token;
		
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException {
		
		usernameService = new GetUsername4TokenService(_token);
		
		idService = new IDRemoteServices();
		
		usernameService.execute();
		
		String username = usernameService.getUsername();
		
		try {
			//remote renew password
			idService.renewPassword(username);
			
			service.setUserPassword(username, null);
			
		} catch (RemoteInvocationException e) {
			
			throw new UnavailableServiceException();
		
		}
		
		service.execute();
		
	}	

}
