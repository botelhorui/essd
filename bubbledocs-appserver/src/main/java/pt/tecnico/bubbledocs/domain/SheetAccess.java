package pt.tecnico.bubbledocs.domain;

public class SheetAccess extends SheetAccess_Base {
    
    public SheetAccess() {
        super();
    }
    
    public void init(User user, SheetData sheetData, boolean canWrite){
    	setUser(user);
    	setSheetData(sheetData);
    	setCanWrite(canWrite);
    }

	public void delete() {
		setUser(null);
		setSheetData(null);
		deleteDomainObject();
	}
    
}
