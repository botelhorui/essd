package pt.ulisboa.tecnico.essd.crypto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

public class CredentialsManager {
	
	
	private static CredentialsManager credentialsManager;
	
	HashMap<String, Credentials> credentials;
	
	public String decorate(String s){
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		Date date = new Date();
		return String.format("[%s][CredentialsManager]", format.format(date))+s;
	}
	
	public void println(String s){
		System.out.println(decorate(s));
	}
	
	public void printf(String s, Object... args){
		System.out.printf(decorate(s), args);
	}
	
	
	private CredentialsManager(){
		credentials = new HashMap<String, Credentials>();
	}
	
	
	public static CredentialsManager getInstance(){		
		if(credentialsManager==null){
			credentialsManager = new CredentialsManager();			
		}		
		return credentialsManager;
	}	
	
	public void addCredentials(String username, byte[] ticketEncrypted, byte[] sessionKey, byte[] encryptionKey){
		if(credentials.containsKey(username)){
			credentials.remove(username);
		}
		
		Credentials c = new Credentials(username, ticketEncrypted, sessionKey, encryptionKey);	
		printf("Added credentials(username=%s, ticketEncrypted=%s, sessionKey=%s, encryptionKey=%s)%n",
				username,
				printBase64Binary(ticketEncrypted),
				printBase64Binary(sessionKey),
				printBase64Binary(encryptionKey));
		credentials.put(username, c);		
	}
	
	public Credentials getCredentials(String username){
		Credentials c = credentials.get(username);
		String out = "";
		if(c==null){
			out="null";
		}else{
			out = String.format("Credentials(username=%s, ticketEncrypted=%s, sessionKey=%s, encryptionKey=%s)",
					username,
					printBase64Binary(c.getTicketEncrypted()),
					printBase64Binary(c.getSessionKey()),
					printBase64Binary(c.getEncryptionKey()));
		}
		printf("getCredentials(usename=%s) = %s%n",username,out);
		return c;
	}
}
