package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.dto.UserDTO;

public class GetUserInfoService extends BubbleDocsService{

	private String username;
	private UserDTO userDTO;
	
	public GetUserInfoService(String username) {
		this.username=username;
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = BubbleDocs.getInstance();
		User user = bd.getUserByUsername(username);
		userDTO = new UserDTO(user);	
	}
	
	UserDTO getResult(){
		return userDTO;
	}

}
