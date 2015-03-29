package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class BFDiv extends BFDiv_Base {

	public BFDiv() {
		super();
	}
	
    public BFDiv(Argument left, Argument right){
    	super();
    	setLeftArgument(left);
    	setRightArgument(right);
    }
	
	@Override
	public Element export(){
    	Element e = new Element("BFDIV");
    	e.addContent(this.getLeftArgument().export());
    	e.addContent(this.getRightArgument().export());
    	return e;
	}
	
}
