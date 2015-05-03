package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public abstract class FunctionContent extends FunctionContent_Base {
    
    public FunctionContent() {
        super();
    }
    
    public Element export(){
    	return null;
	}
    
    public void delete(){
    	super.delete();
    }
    
}
