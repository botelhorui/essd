package pt.tecnico.bubbledocs.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;

public class Session extends Session_Base {
    
    public Session() {
        super();
    }
    
    public void init(DateTime lastAccess, String token){
    	setLastAccess(lastAccess);
    	setToken(token);
    }
    
    public void renewLassAccess(){
    	setLastAccess(new DateTime());
    }
    
    public void renewToken(){
    	String token;
		int rn;
		if(getToken()==null){
			rn = (int)(Math.random()*9);
			token = getUser().getUsername()+rn;
			setToken(token);
			return;
		}
		Pattern p = Pattern.compile("(\\d)$");
		Matcher m = p.matcher(getToken());
		m.find();
		int n = Integer.parseInt(m.group());		
		while(true){
			rn = (int)(Math.random()*9);
			if(rn!=n){
				token = getUser().getUsername()+rn;
				setToken(token);
				return;
			}			
		}
    }

	public void delete() {
		setUser(null);
		
		deleteDomainObject();
	}
}
