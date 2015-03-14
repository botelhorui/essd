package pt.tecnico.bubbledocs.domain;

import java.util.List;
import java.util.stream.Collectors;

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
    
    public List<SpreadSheet> getCreatedSpreadSheetByName(String name){
    	return getCreatedSheetSet().stream()
    			.filter(ds -> ds.getName().equals(name))
    			.collect(Collectors.toList()); 
    }
    
    public SpreadSheet createSheet(String name,int lines,int columns){
    	SpreadSheet SpreadSheet = new SpreadSheet();
    	SpreadSheet.init(this, name, lines, columns);
    	return SpreadSheet;
    }
}
