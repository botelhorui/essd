package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public abstract class Content extends Content_Base {
    
    protected Content() {
        super();
    }
    
    // more Content methods
    public Element export(){
    	return null;
	}
    
    public void delete(){
    	//Delete Roles
    	setCell(null);
    	
    	//DeleteObject
    	deleteDomainObject();
    }
    
}
