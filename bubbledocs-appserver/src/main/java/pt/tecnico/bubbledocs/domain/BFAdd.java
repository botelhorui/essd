package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class BFAdd extends BFAdd_Base {
    
    public BFAdd() {
        super();
    }
    
    public BFAdd(Argument right, Argument left){
    	super();
    	setLeftArgument(left);
    	setRightArgument(right);
    }
    
    @Override
    public Element export(){
    	Element e = new Element("=ADD");
    	e.addContent(this.getLeftArgument().export());
    	e.addContent(this.getRightArgument().export());
    	return e;
	}
    
}
