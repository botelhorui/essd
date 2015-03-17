package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

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
    
    public Element export(){
    	Element e = new Element("Literal");
    	e.setAttribute("value", "" + this.getValue());
    	return e;
	}
    
}
