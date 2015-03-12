package pt.tecnico.bubbledocs.domain;

public class User extends User_Base {
    
    public User() {
        super();
    }
    
    public void init(String username,String password,String name){
    	setUsername(username);
    	setPassword(password);
    	setName(name);    	
    	
    }   
    
    public void delete(){
    	for(SheetAccess sa: getSheetAccessSet())
    		sa.delete();
    	setBubbleDocs(null);
    	//todo
    	deleteDomainObject();
    }
    
    @Override
    public void setUsername(String username) {
    	// TODO Auto-generated method stub
    	super.setUsername(username);
    }
    
    public SheetAccess getSheetAccessByName(String name){
    	for(SheetAccess x: getSheetAccessSet()){
    		if(x.getSheetData().getName().equals(name)){
    			return x;
    		}
    	}
    	return null;
    }
    
    public SheetAccess createSheet(String name,int lines,int columns){
    	SheetData sheetData = new SheetData();
    	sheetData.init(this, name, lines, columns);
    	SheetAccess sheetAccess = new SheetAccess();
    	sheetAccess.init(this,sheetData,true);
    	addSheetAccess(sheetAccess);
    	addCreatedSheet(sheetData);
    	return sheetAccess;
    }
}
