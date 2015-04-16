package pt.tecnico.bubbledocs.service;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.domain.SpreadSheet;

import pt.tecnico.bubbledocs.exception.UserHasNotWriteAccessException;
import pt.tecnico.bubbledocs.exception.UserHasNotReadAccessException;

// add needed import declarations

public abstract class AccessBubbleDocsService extends LoggedBubbleDocsService{

    protected abstract void dispatch() throws BubbleDocsException;
    
    protected void checkWritePermission(String token, SpreadSheet s) throws BubbleDocsException{
    	
    	BubbleDocs bd = BubbleDocs.getInstance();
    	User writer = bd.getUserByToken(token);

		if(writer.checkWriteAccess(s) == false){
			throw new UserHasNotWriteAccessException();
		}
    	
    }
    
    protected void checkReadPermission(String token, SpreadSheet s) throws BubbleDocsException{
    	
    	BubbleDocs bd = BubbleDocs.getInstance();
    	User reader = bd.getUserByToken(token);

		if(reader.checkReadAccess(s) == false){
			throw new UserHasNotReadAccessException();
		}
    	
    }
    
    
}
