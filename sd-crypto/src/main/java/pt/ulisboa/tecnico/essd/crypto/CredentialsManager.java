package pt.ulisboa.tecnico.essd.crypto;

import java.util.Map;
import java.util.HashMap;


public class CredentialsManager {
	
	
	private static CredentialsManager credentialsManager;
	
	HashMap<String, Credentials> credentials;
	
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
		
		Credentials c = new Credentials(username, ticketEncrypted, sessionKey, encryptionKey);
		
		if(credentials.containsKey(username)){
			credentials.remove(username);
		}
		
		credentials.put(username, c);
		
	}
	
	
	public Credentials getCredentials(String username){
		
		return credentials.get(username);
		
	}
	

}
