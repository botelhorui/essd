package pt.tecnico.bubbledocs.domain;

import pt.ist.fenixframework.FenixFramework;
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
	
}
