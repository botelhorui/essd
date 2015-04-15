package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public abstract class Argument extends Argument_Base {
    
    protected Argument() {
        super();
    }
    
    // more Argument methods
    public Element export(){
    	return null;
	}
    
    protected void delete(){
    	//Delete Roles
    	setRightBinaryFunction(null);
    	setLeftBinaryFunction(null);
    	
    	//Delete Object
    	deleteDomainObject();
    }
    
    public abstract int getValue();
    
}
