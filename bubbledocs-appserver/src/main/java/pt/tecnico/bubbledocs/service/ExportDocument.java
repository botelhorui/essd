package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

// add needed import declarations

public class ExportDocument extends BubbleDocsService {
	private byte[] docXML;
	private String token;

	public byte[] getDocXML() {
		return docXML;
	}

	public ExportDocument(String userToken, int docId) {
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
}
