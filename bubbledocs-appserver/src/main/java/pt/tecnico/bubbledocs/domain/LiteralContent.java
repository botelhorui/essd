package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

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
    @Override
    public Element export(){
    	Element e = new Element("Literal");
    	e.setAttribute("value", "" + this.getValue());
    	return e;
	}
    
}
