package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exception.DivisionByZeroException;

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
	
	public int getValue(){
    	int a = getLeftArgument().getValue();
    	int b = getRightArgument().getValue();
    	if (b == 0){
    		throw new DivisionByZeroException();
    	} else {
    		return b/a;
    	}
    	
    }
}
