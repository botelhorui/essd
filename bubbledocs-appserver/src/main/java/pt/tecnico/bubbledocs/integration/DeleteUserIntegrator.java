package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.service.GetUserInfoService;
import pt.tecnico.bubbledocs.service.dto.UserDTO;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.DeleteUser;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;


public class DeleteUserIntegrator extends BubbleDocsIntegrator {

	private String rootToken;
	private DeleteUser service;
	private String toDeleteUsername;
	private IDRemoteServices idService;

	public DeleteUserIntegrator(String token, String toDeleteUsername) {

		rootToken = token;
		service = new DeleteUser(token, toDeleteUsername);
		this.toDeleteUsername = toDeleteUsername;

	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		idService = new IDRemoteServices();
		GetUserInfoService guis = new GetUserInfoService(toDeleteUsername);
		guis.execute();
		UserDTO userInfo = guis.getResult();
		
		service.execute();
		
		try {

			idService.removeUser(toDeleteUsername);

		} catch (Exception e) {
			CreateUser rollback = new CreateUser(rootToken, userInfo.getUsername(), userInfo.getName(), userInfo.getEmail());
			rollback.execute();
			throw new UnavailableServiceException();
		}

		
		
	}	

}
