package pt.tecnico.bubbledocs.domain;

public class SheetAccess extends SheetAccess_Base {
    
    public SheetAccess() {
        super();
    }
    
    public SheetAccess(User user, SpreadSheet spread){
    	super();
    	init(user, spread);
    }
    
    protected void init(User user, SpreadSheet spread){
    	setUser(user);
    	setSpreadSheet(spread);
    }

	public void delete() {
		//Delete Roles
		setUser(null);
		setSpreadSheet(null);
		
		//Delete Object
		deleteDomainObject();
	}
    
}
