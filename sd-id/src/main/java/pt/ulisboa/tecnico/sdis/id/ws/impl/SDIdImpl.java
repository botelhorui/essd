package pt.ulisboa.tecnico.sdis.id.ws.impl;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Arrays;
import pt.ulisboa.tecnico.sdis.id.ws.impl.UserAccount;
import javax.jws.*;
import pt.ulisboa.tecnico.sdis.id.ws.*; // classes generated from WSDL

@WebService(
	endpointInterface="pt.ulisboa.tecnico.sdis.id.ws.SDId", 
	wsdlLocation="SD-ID.1_1.wsdl",
	name = "SDId",
	portName="SDIdImplPort",
	targetNamespace="urn:pt:ulisboa:tecnico:sdis:id:ws",
	serviceName="SDId"
)

public class SDIdImpl implements SDId{

	private ArrayList<UserAccount> _users = new ArrayList<UserAccount>();
	
	public SDIdImpl(){
		populateDomain();
	}
	
	private void populateDomain(){
		UserAccount ua = new UserAccount("alice", "Aaa1", "alice@tecnico.pt");
		_users.add(ua);
		
		ua = new UserAccount("bruno", "Bbb2", "bruno@tecnico.pt");
		_users.add(ua);
		
		ua = new UserAccount("carla", "Ccc3", "carla@tecnico.pt");
		_users.add(ua);
		
		ua = new UserAccount("duarte", "Ddd4", "duarte@tecnico.pt");
		_users.add(ua);
		
		ua = new UserAccount("eduardo", "Eee5", "eduardo@tecnico.pt");
		_users.add(ua);
	}
	
	@Override
	public void createUser(String userId, String emailAddress)
			throws EmailAlreadyExists_Exception, InvalidEmail_Exception,
			InvalidUser_Exception, UserAlreadyExists_Exception{
		
		//Verifications
		
		for (UserAccount ut : _users){
			if(ut.getEmail().equals(emailAddress))
				throw new EmailAlreadyExists_Exception("The email '"+emailAddress+"' already exists", null);
		}
		
		for (UserAccount ut : _users){
			if(ut.getUserId().equals(userId))
				throw new UserAlreadyExists_Exception("The user '"+userId+"' already exists", null);
		}
		
		if(userId == null || userId.equals(""))
			throw new InvalidUser_Exception("The received user is invalid: either null or empty", null);
		
		StringTokenizer st = new StringTokenizer(emailAddress, "@", false);
		if(st.countTokens() != 2)
			throw new InvalidEmail_Exception("The email '"+emailAddress+"' is invalid", null);
		
		//Create the user
		
		UserAccount ua = new UserAccount(userId, emailAddress);
		_users.add(ua);
		
		System.out.println("Generated password: " + ua.getPassword());
	}
	
	@Override
	public void renewPassword(String userId)
	        throws UserDoesNotExist_Exception{
		
		//Retrieve user and verify if it exists
		UserAccount ua = null;
		for (UserAccount ut : _users){
			if(ut.getUserId().equals(userId)){
				ua = ut;
				break;
			}
		}
		
		if(ua == null)
			throw new UserDoesNotExist_Exception("The user '"+userId+"' does not exist", null);
		
		//Generate new password
		
		String new_password = ua.generatePassword();
		
		System.out.println("Generated password: " + new_password);
	}
	
	@Override
	public void removeUser(String userId)
	        throws UserDoesNotExist_Exception{
		
		//Remove user if it exists
		UserAccount ua = null;
		for (UserAccount ut : _users){
			if(ut.getUserId().equals(userId)){
				ua = ut;
				_users.remove(ua);
				return;
			}
		}
		
		if(ua == null)
			throw new UserDoesNotExist_Exception("The user '"+userId+"' does not exist", null);
	}
	
	
	@Override
	public byte[] requestAuthentication(String userId, byte[] reserved)
	        throws AuthReqFailed_Exception{
		
		UserAccount ua = null;
		for (UserAccount ut : _users){
			if(ut.getUserId().equals(userId)){
				ua = ut;
				break;
			}
		}
		
		if(ua == null)
			throw new AuthReqFailed_Exception("Authentication failed: user doesn't exist", null);
		
		String stored_password = ua.getPassword();
		byte[] sp = stored_password.getBytes();
		
		if(Arrays.equals(sp, reserved) == false)
			throw new AuthReqFailed_Exception("Authentication failed: received password doesn't match stored password", null);
		
		byte[] bytes = new byte[1];
		Arrays.fill( bytes, (byte) 1 );
		
		return bytes;
		
	}
}