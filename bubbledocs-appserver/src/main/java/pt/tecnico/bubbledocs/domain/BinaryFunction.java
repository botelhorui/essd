package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class BinaryFunction extends BinaryFunction_Base {
    
    public BinaryFunction() {
        super();
    }
    
    public BinaryFunction(Argument right, Argument left){
    	super();
    	setLeftArgument(left);
    	setRightArgument(right);
    }
    
    public Element export(){
    	return null;
	}
    
}
