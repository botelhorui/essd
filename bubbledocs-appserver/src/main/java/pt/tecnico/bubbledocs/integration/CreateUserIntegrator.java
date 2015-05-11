package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.service.DeleteUser;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class CreateUserIntegrator extends BubbleDocsIntegrator {

	private CreateUser service;
	private IDRemoteServices idService;
	private String rootToken;
	
	public CreateUserIntegrator(String token, String username, String name, String email) {

		service = new CreateUser(token,username,name,email);
		rootToken = token;

	}

	@Override
	protected void dispatch() throws BubbleDocsException {

		idService = new IDRemoteServices();
		
		service.execute();

		try {

			idService.createUser(service.getUsername(), service.getName(), service.getEmail());

		} catch (RemoteInvocationException e) {
			
			DeleteUser rollback = new DeleteUser(rootToken, service.getUsername());
			rollback.execute();

			throw new UnavailableServiceException();

		}
				

	}	

}
