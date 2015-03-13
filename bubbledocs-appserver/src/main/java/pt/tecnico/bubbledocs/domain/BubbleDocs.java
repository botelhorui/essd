package pt.tecnico.bubbledocs.domain;

import org.jdom2.Document;
import org.jdom2.Element;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.DifferentUserImportException;
import pt.tecnico.bubbledocs.exception.UserDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UsernameAlreadyExistsException;

public class BubbleDocs extends BubbleDocs_Base {
    
    private BubbleDocs() {        
        FenixFramework.getDomainRoot().setBubbleDocs(this);            
    }
    
	public static BubbleDocs getInstance(){
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbleDocs();
		if(bd==null){
			bd = new BubbleDocs();
		}
	
		return bd;
	}
	

	
	public int generateId(){
		int genId = getGenId();
		setGenId(genId+1);
		return genId;
	}
	
	public User getUserByUsername(String username){
		for(User u: getUserSet()){
			if(u.getUsername().equals(username)){
				return u;
			}
		}
		return null;
	}
	
	public boolean hasUser(String username){
		return getUserByUsername(username) != null;
	}
	
	@Override
    public void addUser(pt.tecnico.bubbledocs.domain.User user) throws UsernameAlreadyExistsException {
        if(hasUser(user.getUsername())){
        	throw new UsernameAlreadyExistsException();
        }
        super.addUser(user);
    }

    public void removeUser(String username) throws UserDoesNotExistException {
    	User u = getUserByUsername(username);
    	if(u == null){
    		throw new UserDoesNotExistException();
    	}
    	super.removeUser(u);
    }
    
    @Atomic
    public void importSheet(Document doc,String username){
    	try{
    		//Every get we use in the xml might return null if the xml is invalid
	    	Element root = doc.getRootElement();
	    	Element creatorEl = root.getChild("Creator");
	    	String importUsername = creatorEl.getAttributeValue("username");
	    	if(!importUsername.equals(username))
	    		throw new DifferentUserImportException();
	    	User creator = getUserByUsername(username);
	    	if(creator == null)
	    		throw new UserDoesNotExistException();
	    	// import SheetData
	    	SheetData sd = new SheetData();
	    	sd.init(creator, root.getAttributeValue("name"),
	    			Integer.parseInt(root.getAttributeValue("lines")),
	    			Integer.parseInt(root.getAttributeValue("columns")));
	    	DateTimeFormatter dtf = ISODateTimeFormat.dateTime();
	    	sd.setCreationDate(dtf.parseDateTime(root.getAttributeValue("creation-date")));
  	
	    	// import all cells in any
	    	Element cells = root.getChild("Cells");
	    	for(Element celle: cells.getChildren()){
	    		Cell cell = new Cell();
	    		cell.init(sd, 
	    				Integer.parseInt(celle.getAttributeValue("line")),
	    				Integer.parseInt(celle.getAttributeValue("column")),
	    				celle.getAttributeValue("text"));    		
	    	}
    	}
    	catch(NullPointerException e){
    		System.out.println("Exception while importing a sheet:");
    		System.out.println(e);
    		throw e;
    	}
    	
    }
	
}
