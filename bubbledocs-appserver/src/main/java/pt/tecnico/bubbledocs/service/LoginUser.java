package pt.tecnico.bubbledocs.service;

import org.joda.time.LocalTime;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnknownBubbleDocsUserException;
import pt.tecnico.bubbledocs.exception.WrongPasswordException;

public class LoginUser extends BubbleDocsService {

    private String userToken;
    private String username;
    private String password;

    public LoginUser(String username, String password) {
		this.username=username;
		this.password=password;
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
    	BubbleDocs bd = BubbleDocs.getInstance();
    	//check if user exists
    	User u = bd.getUserByUsername(username);
    	if(u==null)
    		throw new UnknownBubbleDocsUserException();
    	//check if user password is correct
    	if(u.getPassword()!=password)
    		throw new WrongPasswordException();
    	//renew session timeout
    	bd.renewSessionDuration(u);    	
    	//renew token
    	bd.renewToken(u);
    	userToken=u.getToken();  
    	
    	bd.cleanInvalidSessions();
    	
    }

    public final String getUserToken() {
    	return userToken;
    }
}
