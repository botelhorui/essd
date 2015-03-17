package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class ReferenceContent extends ReferenceContent_Base {
    
    public ReferenceContent() {
        super();
    }
    
    public ReferenceContent(Cell cell){
    	super();
    	setReferenceCell(cell);
    }
    
    public Element export(){
    	Element e = new Element("Reference");
    	e.addContent(this.getReferenceCell());
    	return e;
	}
    
}
