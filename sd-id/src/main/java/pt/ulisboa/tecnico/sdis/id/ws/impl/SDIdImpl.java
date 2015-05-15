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

import java.util.Date;
import java.text.DateFormat;
import java.util.Calendar;
import java.text.SimpleDateFormat;


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

	private String _sStoreKey = "7o4+TVh/u+0je+qKK9UkPg==";

	public String decorate(String s){
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		Date date = new Date();
		return String.format("[%s][SDIdImpl]", format.format(date))+s;
	}
	
	public void println(String s){
		System.out.println(decorate(s));
	}
	
	public void printf(String s, Object... args){
		System.out.printf(decorate(s), args);
	}
	
	public SDIdImpl() throws AuthReqFailed_Exception{
		populateDomain();
	}

	private void populateDomain() throws AuthReqFailed_Exception{
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
		UserAccount ua;
		try{
			ua = new UserAccount(userId, emailAddress);
			_users.add(ua);
		}catch(AuthReqFailed_Exception e){
			throw new InvalidUser_Exception("Encryption Key for user could not be generated", new InvalidUser());
		}

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
			printf("ReservedXML:%n%s%n",new String(res.encode()));
		}catch(Exception e){
			throw new AuthReqFailed_Exception("Parsing ReservedXML wrong: " + e.getMessage(), new AuthReqFailed());
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
			encryptionKey = ua.getEncryptionKey();
			storeKey = DatatypeConverter.parseBase64Binary(_sStoreKey);
		}catch(Exception e){
			throw new AuthReqFailed_Exception("Wrong clientKey or storeKey: " + e.getMessage(), new AuthReqFailed());
		}

		//Generate and encrypt UserCredentials
		UserCredentials uc = new UserCredentials(sessionKey, encryptionKey, nounce);
		printf("UserCredentials:%n%s%n",new String(uc.encode()));
		byte[] bUC = uc.encode();
		byte[] bEncUC;
		try{
			bEncUC = aes.encrypt(bUC, clientKey);
		}catch(Exception e){
			throw new AuthReqFailed_Exception("Error: encrypting UserCredentials: " + e.getMessage(), new AuthReqFailed());
		}

		//Generate and encrypt Kerberos Ticket

		//create an instance of SimpleDateFormat used for formatting 
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();

		// Get the initial time stamp
		Date initial = cal.getTime();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR_OF_DAY, 2);
		Date end = cal.getTime();
		// Using DateFormat format method we can create a string representation of a date with the defined format
		String sInitial = df.format(initial);
		String sEnd = df.format(end);

		Ticket ticket = new Ticket(userId, "SD-STORE", sInitial, sEnd, sessionKey);
		printf("UserCredentials:%n%s%n",new String(ticket.encode()));
		byte[] bTicket = ticket.encode();
		byte[] bEncTicket;
		try{
			bEncTicket = aes.encrypt(bTicket, storeKey);
		}catch(Exception e){
			throw new AuthReqFailed_Exception("Error: encrypting Ticket: " + e.getMessage(), new AuthReqFailed());
		}

		//Generate RequestAuthenticationResponse and return it in byte[]

		RequestAuthenticationResponse rar = new RequestAuthenticationResponse(bEncTicket, bEncUC);
		printf("UserCredentials:%n%s%n",new String(rar.encode()));
		return rar.encode();

	}
}