package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnknownBubbleDocsUserException;
import pt.tecnico.bubbledocs.exception.WrongPasswordException;

public class AssignLiteralCell extends BubbleDocsService {
	private String result;
	private String token;

	public AssignLiteralCell(String token, int docId, String cellId,
			String literal) {
		// add code here
		this.token=token;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = BubbleDocs.getInstance();
		//check if user exists
		User u = bd.getUserByToken(token);
		if(u==null)
			throw new UnknownBubbleDocsUserException();
		// TODO check permissions
		//renew session timeout
		bd.renewSessionDuration(u);    	
	}

	public String getResult() {
		return result;
	}

}
