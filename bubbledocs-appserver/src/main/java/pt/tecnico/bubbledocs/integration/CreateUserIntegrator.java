package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.domain.BubbleDocs;

public class CreateUserIntegrator extends BubbleDocsIntegrator {

	private CreateUser service;

	public CreateUserIntegrator(String token, String username, String name, String email) {

		service = new CreateUser(token,username,name,email);

	}

	@Override
	protected void dispatch() throws BubbleDocsException {

		BubbleDocs bd = BubbleDocs.getInstance();

		try {

			bd.IDRemoteServices.createUser(service.getUsername(), service.getName(), service.getEmail());

		} catch (RemoteInvocationException e) {

			throw new UnavailableServiceException();

		}
		
		service.execute();

	}	



}
