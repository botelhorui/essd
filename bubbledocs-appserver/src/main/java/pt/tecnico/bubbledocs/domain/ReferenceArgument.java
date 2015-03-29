package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class ReferenceArgument extends ReferenceArgument_Base {
    
    public ReferenceArgument() {
        super();
    }
    
    public ReferenceArgument(Cell cell) {
        super();
        setReference(cell);
    }
    
    public Element export(){
    	Element e = new Element("Reference");
    	e.addContent(this.getReference().export());
    	return e;
	}
    
    public void delete(){
    	setReference(null);
    	super.delete();
    }
    
}
