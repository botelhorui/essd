package pt.ulisboa.tecnico.essd.crypto;

import java.util.Map;


public class CredentialsManager {
	
	
	private static CredentialsManager credentialsManager;
	
	Map<String, Credentials> credentials;
	
	
	public static CredentialsManager getInstance(){
		
		if(credentialsManager==null){
			credentialsManager = new CredentialsManager();
			
		}		
		return credentialsManager;
	}
	
	
	void addCredential(String username, byte[] ticket, byte[] clientKey, byte[] encryptionKey){
		
		Credentials c = new Credentials(username, ticket, clientKey, encryptionKey);
		credentials.put(username, c);
		
	}
	
	
	

}
