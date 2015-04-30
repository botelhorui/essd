package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.service.RenewPassword;

public class RenewPasswordIntegrator extends BubbleDocsIntegrator {
	
	private RenewPassword service;
	private String token;
	
	public RenewPasswordIntegrator(String token) {
		
		service = new RenewPassword(token);
		this.token = token;
		
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException {

		BubbleDocs bd = BubbleDocs.getInstance();
		User user = bd.getUserByToken(token);
		
		try {
			//remote renew password
			bd.IDRemoteServices.renewPassword(user.getUsername());
			user.setPassword(null);
			
		} catch (RemoteInvocationException e) {
			
			throw new UnavailableServiceException();
		
		}
		
		service.execute();
		
	}	

}
