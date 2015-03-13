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
    	for(SheetData sd: getSheetDataSet())
    		sd.delete();
    	setBubbleDocs(null);
    	deleteDomainObject();
    }
    
    @Override
    public void setUsername(String username) {
    	// TODO Auto-generated method stub
    	super.setUsername(username);
    }
    
    public List<SheetAccess> getSheetAccessByName(String name){
    	return getSheetAccessSet().stream()
    			.filter(as -> as.getSheetData().getName().equals(name))
    			.collect(Collectors.toList());    	
    }
    
    public List<SheetData> getCreatedSheetDataByName(String name){
    	return getCreatedSheetSet().stream()
    			.filter(ds -> ds.getName().equals(name))
    			.collect(Collectors.toList()); 
    }
    
    public SheetData createSheet(String name,int lines,int columns){
    	SheetData sheetData = new SheetData();
    	sheetData.init(this, name, lines, columns);
    	return sheetData;
    }
}
