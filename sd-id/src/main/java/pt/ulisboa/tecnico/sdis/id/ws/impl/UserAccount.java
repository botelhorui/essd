package pt.ulisboa.tecnico.sdis.id.ws.impl;

import java.util.Random;

public class UserAccount{
	private final static int MIN = 1000000;
	private final static int MAX = 9999999;
	
	private String _userId;
	private String _password;
	private String _email;
	
	public UserAccount(String userId, String email){
		this._userId = userId;
		this._email = email;
		this._password = this.generatePassword();
	}
	
	public UserAccount(String userId, String password, String email){
		this._userId = userId;
		this._email = email;
		this._password = password;
	}
	
	//generates a new password and replace the current one with the new one
	//returns the generated password
	public String generatePassword(){
		String old_password = _password;
		//Generates a new Random object
		Random random = new Random();
		
	    // generates a Random int between MIN and MAX
		// the plus 1 is to make it inclusive with the top value
	    int number = random.nextInt((MAX - MIN) + 1) + MIN;
	    String new_password = Integer.toString(number);
	    
	    //to prevent the new_password being equal to the old_password
	    while(new_password.equals(old_password)){
	    	number = random.nextInt((MAX - MIN) + 1) + MIN;
		    new_password = Integer.toString(number);
	    }
	    
	    this.setPassword(new_password);
	    
	    return new_password;
	}
	
	public String getUserId(){
		return _userId;
	}
	
	public void setUserId(String userId){
		_userId = userId;
	}
	
	public String getPassword(){
		return _password;
	}
	
	public void setPassword(String password){
		_password = password;
	}
	
	public String getEmail(){
		return _email;
	}
	
	public void setEmail(String email){
		_email = email;
	}
	
}