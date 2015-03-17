package pt.tecnico.bubbledocs.domain;

public class SheetAccess extends SheetAccess_Base {
    
    public SheetAccess() {
        super();
    }
    
    public void init(User user, SpreadSheet SpreadSheet, boolean canWrite){
    	setUser(user);
    	setSpreadSheet(SpreadSheet);
    	//setCanWrite(canWrite);
    }

	public void delete() {
		setUser(null);
		setSpreadSheet(null);
		deleteDomainObject();
	}
    
}
