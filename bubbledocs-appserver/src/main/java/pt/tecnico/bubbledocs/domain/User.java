package pt.tecnico.bubbledocs.domain;

import java.util.List;
import java.util.stream.Collectors;

import pt.tecnico.bubbledocs.exception.UserDoesNotExistException;
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
		//Delete Roles
		for(SheetAccess sa: getSheetAccessSet()){
			sa.delete();
		}
		
		for(SpreadSheet s: getOwnedSpreadSet()){
			removeOwnedSpread(s);
			s.delete();
		}
		
		for(SpreadSheet rs: getReadableSpreadSet()){
			removeReadableSpread(rs);
		}
		
		for(SpreadSheet ws: getWritableSpreadSet()){
			removeWritableSpread(ws);
		}
		
		setBubbleDocs(null);

		//Delete Object
		deleteDomainObject();
	}
    
    @Override
    public void setUsername(String username) {
    	// TODO Auto-generated method stub
    	super.setUsername(username);
    }
    
    public List<SpreadSheet> getOwnedSpreadByName(String name) throws UserIsNotOwnerException {
    	
    	List<SpreadSheet> spreadsheet = getOwnedSpreadSet().stream()
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
