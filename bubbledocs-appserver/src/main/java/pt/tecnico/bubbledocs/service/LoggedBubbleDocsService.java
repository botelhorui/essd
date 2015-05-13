package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.domain.BubbleDocs;

// add needed import declarations

public abstract class LoggedBubbleDocsService extends BubbleDocsService{

    protected abstract void dispatch() throws BubbleDocsException;
    
    protected void validateUser(String token) throws BubbleDocsException{
    	
    	BubbleDocs bd = BubbleDocs.getInstance();
    	bd.validateUser(token);
    	
    }
    
}
