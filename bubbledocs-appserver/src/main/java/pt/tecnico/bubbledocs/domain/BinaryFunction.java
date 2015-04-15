package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public abstract class BinaryFunction extends BinaryFunction_Base {
    
    public BinaryFunction() {
        super();
    }
    
    public Element export(){
    	return null;
	}
    
    public void delete(){
    	//Delete Argument Objects
    	getRightArgument().delete();
    	getLeftArgument().delete();
    	
    	//Delete Roles
    	setRightArgument(null);
    	setLeftArgument(null);
    	
    	super.delete();
    }
    
    public abstract int getValue();
    
}
