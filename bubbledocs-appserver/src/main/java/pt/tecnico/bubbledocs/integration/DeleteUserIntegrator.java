package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.DeleteUser;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;


public class DeleteUserIntegrator extends BubbleDocsIntegrator {

	private DeleteUser service;
	private String toDeleteUsername;
	private IDRemoteServices idService;

	public DeleteUserIntegrator(String token, String toDeleteUsername) {

		service = new DeleteUser(token, toDeleteUsername);
		this.toDeleteUsername = toDeleteUsername;

	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		idService = new IDRemoteServices();
		
		try {

			idService.removeUser(toDeleteUsername);

		} catch (Exception e) {

			throw new UnavailableServiceException();
		}

		service.execute();
		
	}	

}
