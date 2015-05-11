package pt.tecnico.bubbledocs.domain;

import java.util.List;
import java.util.stream.Collectors;

import pt.tecnico.bubbledocs.domain.SpreadSheet;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.UserHasNotWriteAccessException;
import pt.tecnico.bubbledocs.exception.UserIsNotOwnerException;


public class User extends User_Base {
     
    public User() {
        super();
    }
    
    public User(String username, String name, String email){
    	super();
    	init(username, name, email);
    }
    
    protected void init(String username, String name, String email){
    	setUsername(username);
    	setEmail(email);
    	setName(name);
    	setSession(null);
    }   
    
	public void delete(){
		//Delete Roles
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
		
		if(this.getSession() != null)
			this.getSession().delete();
		setSession(null);

		//Delete Object
		deleteDomainObject();
	}
    
	
    @Override
    public void setUsername(String username) throws BubbleDocsException {
    	
    	BubbleDocs bd = BubbleDocs.getInstance();
    	
    	bd.validateUsername(username);
    	
    	if (bd.hasUser(username)){
    		throw new DuplicateUsernameException();
    	}
	
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
    
    public boolean checkIfUserOwnsSpread(SpreadSheet spread){
    	return spread.getOwner().equals(this);
    }
    
	public boolean checkWriteAccess(SpreadSheet spread){
		for(SpreadSheet ws: getWritableSpreadSet()){
			if(spread.equals(ws))
				return true;
		}

		return false;

	}

	public boolean checkReadAccess(SpreadSheet spread){
		for(SpreadSheet rs: getReadableSpreadSet()){
			if(spread.equals(rs))
				return true;
		}

		return false;
	}

	public void addUserToSpreadWriteAccessSet(SpreadSheet spread, User user) throws UserHasNotWriteAccessException, UserIsNotOwnerException{
		if(this.checkIfUserOwnsSpread(spread) || this.checkWriteAccess(spread)){
			if(user.checkWriteAccess(spread) == false){
				user.addWritableSpread(spread);
				spread.addWriterUser(user);
			}
			if(user.checkReadAccess(spread) == false){
				user.addReadableSpread(spread);
				spread.addReaderUser(user);
			}
		}else{
			if(this.checkWriteAccess(spread) == false)
				throw new UserHasNotWriteAccessException();
			else if(this.checkIfUserOwnsSpread(spread) == false)
				throw new UserIsNotOwnerException();
		}
	}

	public void removeUserFromSpreadWriteAccessSet(SpreadSheet spread, User user){
		if(this.checkIfUserOwnsSpread(spread) || this.checkWriteAccess(spread)){
			for(SpreadSheet ws: getWritableSpreadSet()){
				if(spread.equals(ws)){
					spread.removeWriterUser(this);
					removeWritableSpread(ws);
				}
			}
		}else{
			if(this.checkWriteAccess(spread) == false)
				throw new UserHasNotWriteAccessException();
			else if(this.checkIfUserOwnsSpread(spread) == false)
				throw new UserIsNotOwnerException();
		}
	}
	
	public void addUserToSpreadReadAccessSet(SpreadSheet spread, User user) throws UserHasNotWriteAccessException, UserIsNotOwnerException{
		if(this.checkIfUserOwnsSpread(spread) || this.checkWriteAccess(spread)){
			if(user.checkReadAccess(spread) == false){
				user.addReadableSpread(spread);
				spread.addReaderUser(user);
			}
		}else{
			if(this.checkWriteAccess(spread) == false)
				throw new UserHasNotWriteAccessException();
			else if(this.checkIfUserOwnsSpread(spread) == false)
				throw new UserIsNotOwnerException();
		}
	}
	
	public void removeUserFromSpreadReadAccessSet(SpreadSheet spread, User user) throws UserHasNotWriteAccessException, UserIsNotOwnerException{
		if(this.checkIfUserOwnsSpread(spread) || this.checkWriteAccess(spread)){
			for(SpreadSheet rs: getReadableSpreadSet()){
				if(spread.equals(rs)){
					spread.removeReaderUser(this);
					removeReadableSpread(rs);
				}
			}
		}else{
			if(this.checkWriteAccess(spread) == false)
				throw new UserHasNotWriteAccessException();
			else if(this.checkIfUserOwnsSpread(spread) == false)
				throw new UserIsNotOwnerException();
		}
	}

	//Overloaded -- could not Override -- some Fenix Framework class has equals as final
	public boolean equals(User that) {
		// Custom equality check here.
		if(this.getUsername().equals(that.getUsername())
				&& this.getName().equals(that.getName())
				&& this.getPassword().equals(that.getPassword())
				&& this.getEmail().equals(that.getEmail()))
			return true;

		return false;
	}
}