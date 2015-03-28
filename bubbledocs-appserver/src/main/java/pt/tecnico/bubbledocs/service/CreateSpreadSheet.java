package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

// add needed import declarations

public class CreateSpreadSheet extends BubbleDocsService {
	private int sheetId;  // id of the new sheet
	private String token;

	public int getSheetId() {
		return sheetId;
	}

	public CreateSpreadSheet(String token, String name, int rows,int columns) {
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
