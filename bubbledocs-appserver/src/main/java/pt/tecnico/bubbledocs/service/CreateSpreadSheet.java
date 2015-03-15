package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnknownBubbleDocsUserException;

// add needed import declarations

public class CreateSpreadSheet extends BubbleDocsService {
    private int sheetId;  // id of the new sheet
	private String token;

    public int getSheetId() {
        return sheetId;
    }

    public CreateSpreadSheet(String token, String name, int rows,
            int columns) {
    	this.token=token;
    	// TODO add code here
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

}
