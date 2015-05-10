package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class CreateUserIntegrator extends BubbleDocsIntegrator {

	private CreateUser service;
	private IDRemoteServices idService;
	
	public CreateUserIntegrator(String token, String username, String name, String email) {

		service = new CreateUser(token,username,name,email);

	}

	@Override
	protected void dispatch() throws BubbleDocsException {

		idService = new IDRemoteServices();

		try {

			idService.createUser(service.getUsername(), service.getName(), service.getEmail());

		} catch (RemoteInvocationException e) {

			throw new UnavailableServiceException();

		}
		
		service.execute();

	}	



}
