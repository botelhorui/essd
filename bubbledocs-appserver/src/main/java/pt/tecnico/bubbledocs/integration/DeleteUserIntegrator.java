package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.DeleteUser;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;


public class DeleteUserIntegrator extends BubbleDocsIntegrator {

	private DeleteUser service;
	private String toDeleteUsername;

	public DeleteUserIntegrator(String token, String toDeleteUsername) {

		service = new DeleteUser(token, toDeleteUsername);
		this.toDeleteUsername = toDeleteUsername;

	}

	@Override
	protected void dispatch() throws BubbleDocsException {

		BubbleDocs bd = BubbleDocs.getInstance();
		
		try {

			bd.IDRemoteServices.removeUser(toDeleteUsername);

		} catch (Exception e) {

			throw new UnavailableServiceException();
		}

		service.execute();
		
	}	

}
