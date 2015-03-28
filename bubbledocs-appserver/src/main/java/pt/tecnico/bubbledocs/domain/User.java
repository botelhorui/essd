package pt.tecnico.bubbledocs.domain;

import java.util.List;
import java.util.stream.Collectors;

import pt.tecnico.bubbledocs.exception.UserIsNotOwnerException;


public class User extends User_Base {
     
    public User() {
        super();
    }
    
    public User(String username, String password, String name){
    	super();
    	init(username, password, name);
    }
    
    protected void init(String username, String password, String name){
    	setUsername(username);
    	setPassword(password);
    	setName(name);
    	setSession(null);
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
    
    
    //Editar para devolver null em vez de excepcao. nao faz sentido.
    
    public List<SpreadSheet> getOwnedSpreadSheetsByName(String name){
    	
    	List<SpreadSheet> spreadSheets = getOwnedSpreadSet().stream()
    			.filter(ds -> ds.getName().equals(name))
    			.collect(Collectors.toList());
    	
    	return spreadSheets;
    }
    
    
    public SpreadSheet createSheet(String name, int lines, int columns){
    	SpreadSheet s = new SpreadSheet(this, name, lines, columns);
    	return s;
    }
}
