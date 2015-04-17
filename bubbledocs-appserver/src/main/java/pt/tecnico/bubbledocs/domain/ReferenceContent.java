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
    
    @Override
    public Element export(){
    	Element e = new Element("Reference");
    	e.addContent(this.getReferenceCell().export());
    	return e;
	}
    
    public void delete(){
    	setReferenceCell(null);
    	super.delete();
    }
    
    public int getValue(){
    	
    	return getReferenceCell().getValue();
    }
    
    public String returnValueAsString(){
    	Cell c = getReferenceCell();
    	String s = c.getLine() + ";" + c.getColumn();
    	return s;
    }
    
}
