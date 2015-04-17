package pt.tecnico.bubbledocs.service;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.domain.BubbleDocs;

// add needed import declarations

public abstract class RootBubbleDocsService extends LoggedBubbleDocsService{

    protected abstract void dispatch() throws BubbleDocsException;
    
    @Override
    protected void validateUser(String token) throws BubbleDocsException{
    	
    	super.validateUser(token);
    	
    	BubbleDocs bd = BubbleDocs.getInstance();
    	bd.checkIfRoot(token);	
    
    }
    
}
