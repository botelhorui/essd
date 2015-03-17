package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class BFDiv extends BFDiv_Base {

	public BFDiv() {
		super();
	}
	
	@Override
	public Element export(){
    	Element e = new Element("=DIV");
    	e.addContent(this.getLeftArgument().export());
    	e.addContent(this.getRightArgument().export());
    	return e;
	}
	
}
