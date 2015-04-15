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
    	Element e = new Element("BFSUB");//
    	e.addContent(this.getLeftArgument().export());
    	e.addContent(this.getRightArgument().export());
    	return e;
	}
    
    public int getValue(){
    	int a = getLeftArgument().getValue(); 
    	int b = getRightArgument().getValue();
    	return a-b;
    }
    
}
