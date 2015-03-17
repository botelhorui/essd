package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class BFMul extends BFMul_Base {
    
    public BFMul() {
        super();
    }
    
    public BFMul(Argument right, Argument left){
    	super();
    	setLeftArgument(left);
    	setRightArgument(right);
    }
    
    @Override
    public Element export(){
    	Element e = new Element("=MUL");
    	e.addContent(this.getLeftArgument().export());
    	e.addContent(this.getRightArgument().export());
    	return e;
	}
    
}
