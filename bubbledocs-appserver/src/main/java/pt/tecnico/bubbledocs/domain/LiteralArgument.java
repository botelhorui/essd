package pt.tecnico.bubbledocs.domain;

public class LiteralArgument extends LiteralArgument_Base {
    
    public LiteralArgument() {
        super();
    }
    
    public LiteralArgument(int value){
    	super();
    	init(value);
    }
    
    //allows to class hierarchy to grow
    protected void init(int value){
    	setValue(value);
    }
    
    //more LiteralArgument methods
    
}
