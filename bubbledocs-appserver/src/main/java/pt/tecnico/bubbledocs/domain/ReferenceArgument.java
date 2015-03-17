package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class ReferenceArgument extends ReferenceArgument_Base {
    
    public ReferenceArgument() {
        super();
    }
    
    public ReferenceArgument(Cell cell) {
        super();
        setReferenceCell(cell);
    }
    
    public Element export(){
    	Element e = new Element("Reference");
    	e.addContent(this.getReferenceCell().export());
    	return e;
	}
    
}
