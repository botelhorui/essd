package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

// add needed import declarations

public class AssignReferenceCell extends BubbleDocsService {
	private String result;
	private String token;

	public AssignReferenceCell(String token, int docId, String cellId,String reference) {
		// add code here
		this.token=token;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = BubbleDocs.getInstance();
		// TODO add code here	
		
		// Session validations and renewals
		if(!bd.isUserInSession(token)){
			throw new UserNotInSessionException();
		}else{
			bd.renewSessionDuration(token);
		}
	}

	public final String getResult() {
		return result;
	}
}
