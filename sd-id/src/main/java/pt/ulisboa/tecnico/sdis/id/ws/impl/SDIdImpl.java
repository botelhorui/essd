package pt.ulisboa.tecnico.sdis.id.ws.impl;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Arrays;

import pt.ulisboa.tecnico.sdis.id.ws.impl.UserAccount;
import pt.ulisboa.tecnico.essd.crypto.AESCipher;
import pt.ulisboa.tecnico.essd.xml.UserCredentials;
import pt.ulisboa.tecnico.essd.xml.Ticket;
import pt.ulisboa.tecnico.essd.xml.ReservedXML;
import pt.ulisboa.tecnico.essd.xml.RequestAuthenticationResponse;
import javax.jws.*;
import javax.xml.bind.DatatypeConverter;
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

	private String _sStoreKey = "LgzxjQVHcp3FQdTyfh78LDI0/GXaWT4ioj4vPJ/om0M=";

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

	public ArrayList<UserAccount> getUsers(){
		return this._users;
	}

	@Override
	public void createUser(String userId, String emailAddress)
			throws EmailAlreadyExists_Exception, InvalidEmail_Exception,
			InvalidUser_Exception, UserAlreadyExists_Exception{

		//Verifications

		for (UserAccount ut : _users){
			if(ut.getEmail().equals(emailAddress))
				throw new EmailAlreadyExists_Exception("The email '"+emailAddress+"' already exists", new EmailAlreadyExists());
		}

		for (UserAccount ut : _users){
			if(ut.getUserId().equals(userId))
				throw new UserAlreadyExists_Exception("The user '"+userId+"' already exists", new UserAlreadyExists());
		}

		if(userId == null || userId.equals(""))
			throw new InvalidUser_Exception("The received user is invalid: either null or empty", new InvalidUser());

		if(emailAddress == null || emailAddress.equals(""))
			throw new InvalidEmail_Exception("The receibed email is invalid: null or empty", new InvalidEmail());

		StringTokenizer st = new StringTokenizer(emailAddress, "@", false);
		if(st.countTokens() != 2)
			throw new InvalidEmail_Exception("The email '"+emailAddress+"' is invalid", new InvalidEmail());

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
			throw new UserDoesNotExist_Exception("The user '"+userId+"' does not exist", new UserDoesNotExist());

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
			throw new UserDoesNotExist_Exception("The user '"+userId+"' does not exist", new UserDoesNotExist());
	}


	@Override
	public byte[] requestAuthentication(String userId, byte[] reserved)
			throws AuthReqFailed_Exception{
		//Process ReservedXML and verify service-name
		ReservedXML res;
		try{
			res = ReservedXML.parse(reserved);
		}catch(Exception e){
			throw new AuthReqFailed_Exception(e.getMessage(), new AuthReqFailed());
		}
		if(!res.getServiceName().equals("SD-ID"))
			throw new AuthReqFailed_Exception("Wrong service-name!", new AuthReqFailed());
		int nounce = res.getNounce();

		//Get UserAccount
		UserAccount ua = null;
		for (UserAccount ut : _users){
			if(ut.getUserId().equals(userId)){
				ua = ut;
				break;
			}
		}
		if(ua == null)
			throw new AuthReqFailed_Exception("Authentication failed: user doesn't exist", new AuthReqFailed());

		//Generate Keys: Client Key from Password, Session Key and Decryption Key parse Store Key
		String stored_password = ua.getPassword();
		byte[] clientKey;
		byte[] sessionKey;
		byte[] encryptionKey;
		byte[] storeKey;
		AESCipher aes;
		try{
			aes = new AESCipher();
			clientKey = aes.createKeyFromPassword(stored_password);
			sessionKey = aes.generateKey();
			encryptionKey = aes.generateKey();
			storeKey = DatatypeConverter.parseBase64Binary(_sStoreKey);
		}catch(Exception e){
			throw new AuthReqFailed_Exception(e.getMessage(), new AuthReqFailed());
		}
		
		//Generate UserCredentials
		UserCredentials uc = new UserCredentials(sessionKey, encryptionKey, nounce);

		//byte[] sp = stored_password.getBytes();
		//if(Arrays.equals(sp, reserved) == false)
		//throw new AuthReqFailed_Exception("Authentication failed: received password doesn't match stored password", new AuthReqFailed());

		byte[] bytes = new byte[1];
		Arrays.fill( bytes, (byte) 1 );

		return bytes;

	}
}