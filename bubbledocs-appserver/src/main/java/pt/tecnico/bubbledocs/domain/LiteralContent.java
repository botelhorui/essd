package pt.tecnico.bubbledocs.domain;

public class LiteralContent extends LiteralContent_Base {
    
    public LiteralContent() {
        super();
    }
    
    public LiteralContent(int value){
    	super();
    	init(value);
    }
    
    //allows to class hierarchy to grow
    protected void init(int value){
    	setValue(value);
    }
    
    //more LiteralContent methods
    
}
