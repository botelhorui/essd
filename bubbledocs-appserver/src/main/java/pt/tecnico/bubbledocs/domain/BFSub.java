package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class BFSub extends BFSub_Base {
    
    public BFSub() {
        super();
    }
    
    public BFSub(Argument left, Argument right){
    	super();
    	setLeftArgument(left);
    	setRightArgument(right);
    }
    
    @Override
    public Element export(){
    	Element e = new Element("SUB");
    	e.addContent(this.getLeftArgument().export());
    	e.addContent(this.getRightArgument().export());
    	return e;
	}
    
}
