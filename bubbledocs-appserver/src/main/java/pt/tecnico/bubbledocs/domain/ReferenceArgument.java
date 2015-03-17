package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class ReferenceArgument extends ReferenceArgument_Base {
    
    public ReferenceArgument() {
        super();
    }
    
    public Element export(){
    	Element e = new Element("Reference");
    	e.addContent(this.getReferenceCell());
    	return e;
	}
    
}
