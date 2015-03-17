package pt.tecnico.bubbledocs.domain;

public class BinaryFunction extends BinaryFunction_Base {
    
    public BinaryFunction() {
        super();
    }
    
    public BinaryFunction(Argument right, Argument left){
    	super();
    	setLeftArgument(left);
    	setRightArgument(right);
    }
    
}
