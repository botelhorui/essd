package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnknownBubbleDocsUserException;

public class ExportDocument extends BubbleDocsService {
    private byte[] docXML;
	private String token;

    public byte[] getDocXML() {
	return docXML;
    }

    public ExportDocument(String token, int docId) {
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
    	// TODO add code here
    }
}
