package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

// add needed import declarations

public class AssignReferenceCell extends BubbleDocsService {
	private String result;
	private String token;
	private int iSpreadId;
	private String sCellId; // "1;1"
	private String sReference;

	public AssignReferenceCell(String token, int docId, String cellId, String reference){
		this.iSpreadId = docId;
		this.sCellId = cellId;
		this.sReference = reference;
		this.token=token;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = BubbleDocs.getInstance();
		
		
		
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
