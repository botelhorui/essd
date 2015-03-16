package pt.tecnico.bubbledocs.domain;

import java.util.List;
import java.util.stream.Collectors;

import pt.tecnico.bubbledocs.exception.UserIsNotOwnerException;

public class User extends User_Base {
    
    public User() {
        super();
    }
    
    public void init(String username,String password,String name){
    	setUsername(username);
    	setPassword(password);
    	setName(name);	
    	setLastAccess(null);
    	setToken(null);
    }   
    
    public void delete(){
    	for(SpreadSheet sd: getSpreadSheetSet())
    		sd.delete();
    	setBubbleDocs(null);
    	deleteDomainObject();
    }
    
    @Override
    public void setUsername(String username) {
    	// TODO Auto-generated method stub
    	super.setUsername(username);
    }
    
    public List<SpreadSheet> getSpreadSheetByName(String name){
    	return getSpreadSheetSet().stream()
    			.filter(as -> as.getName().equals(name))
    			.collect(Collectors.toList());    	
    }
   
    public List<SpreadSheet> getCreatedSpreadSheetByName(String name) throws UserIsNotOwnerException {
    	
    	List<SpreadSheet> spreadsheet = getCreatedSheetSet().stream()
    			.filter(ds -> ds.getName().equals(name))
    			.collect(Collectors.toList());
    	
    	if (spreadsheet != null)
    		return spreadsheet;
    	else  
    		throw new UserIsNotOwnerException();
    }
    
    
    public SpreadSheet createSheet(String name,int lines,int columns){
    	SpreadSheet SpreadSheet = new SpreadSheet();
    	SpreadSheet.init(this, name, lines, columns);
    	return SpreadSheet;
    }
}
