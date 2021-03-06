package pt.ulisboa.tecnico.sdis.id.ws.cli;

import javax.xml.bind.DatatypeConverter;
import javax.xml.registry.JAXRException;
import javax.xml.ws.*;

import pt.ulisboa.tecnico.essd.crypto.AESCipher;
import pt.ulisboa.tecnico.essd.crypto.CredentialsManager;
import pt.ulisboa.tecnico.essd.xml.ReservedXML;
import pt.ulisboa.tecnico.essd.xml.RequestAuthenticationResponse;
import pt.ulisboa.tecnico.essd.xml.UserCredentials;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.Arrays;

import example.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.id.ws.*; // classes generated from WSDL

/**
 *  SD-Id client.
 *
 *  Adds easier endpoint address configuration
 *  to the PortType generated by wsimport.
 *
 *  tried to extended to add UDDI lookup capability.
 */

public class SDIdClient implements SDId {

	/** WS service */
	SDId_Service service = null;
	/** WS port (interface) */
	SDId port = null;

	public String decorate(String s){
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		Date date = new Date();
		return String.format("[%s][SDIdClient]", format.format(date))+s;
	}
	
	public void println(String s){
		System.out.println(decorate(s));
	}
	
	public void printf(String s, Object... args){
		System.out.printf(decorate(s), args);
	}
	
	/** constructor with provided web service URL 
	 * @throws JAXRException */
	public SDIdClient(String uddiURL, String wsName) throws SDIdClientException, JAXRException{
		UDDINaming uddiNaming = new UDDINaming(uddiURL);
		String endpointAddress = uddiNaming.lookup(wsName);

		if (endpointAddress == null) {
			throw new SDIdClientException("Couldn't find "+wsName+" Web Service at UDDI.");
		}else{
			println("Found endpoint url: "+endpointAddress);
		}

		service = new SDId_Service();
		port = service.getSDIdImplPort();

		println("Setting endpoint address ...");
		System.out.println(endpointAddress);
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
	}


	// SDId webservice

	public void createUser(String userId, String emailAddress)
			throws EmailAlreadyExists_Exception, InvalidEmail_Exception,
			InvalidUser_Exception, UserAlreadyExists_Exception{
		port.createUser(userId, emailAddress);
	}

	public void renewPassword(String userId)
			throws UserDoesNotExist_Exception{
		port.renewPassword(userId);
	}

	public void removeUser(String userId)
			throws UserDoesNotExist_Exception{
		port.removeUser(userId);
	}

	public byte[] requestAuthentication(String userId, byte[] reserved)
			throws AuthReqFailed_Exception{

		//Generate Client Key
		String pass = new String(reserved);
		printf("requestAuthentication(%s,%s)%n",userId,pass);
		byte[] clientKey;
		AESCipher aes;
		try{
			aes = new AESCipher();
			clientKey = aes.createKeyFromPassword(pass);
		}catch(Exception e){
			throw new AuthReqFailed_Exception("Create clientKey from password: " + e.getMessage(), new AuthReqFailed());
		}
		
		//Generate nounce
		Random random = new Random();
		int MIN = 1;
		int MAX = 999999;
		// generates a Random int between MIN and MAX
		// the plus 1 is to make it inclusive with the top value
		int nounce = random.nextInt((MAX - MIN) + 1) + MIN;
		
		//Generate Reserved
		ReservedXML res = new ReservedXML("SD-ID", nounce);
		printf("ReservedXML:%n%s%n",new String(res.encode()));
		byte[] bRes = res.encode();
		
		//Receive Response
		byte[] bResponse = port.requestAuthentication(userId, bRes);
		//Process response
		RequestAuthenticationResponse response;
		byte[] bEncTicket;
		byte[] bEncUserCredentials;
		byte[] bUserCredentials;
		UserCredentials user;
		try{
			response = RequestAuthenticationResponse.parse(bResponse);
			printf("RequestAuthenticationResponse:%n%s%n",new String(response.encode()));
			bEncTicket = response.getEncryptedTicket();
			bEncUserCredentials = response.getEncryptedUserCredentials();
			bUserCredentials = aes.decrypt(bEncUserCredentials, clientKey);
			user = UserCredentials.parse(bUserCredentials);
			printf("UserCredentials:%n%s%n",new String(user.encode()));
		}catch(Exception e){
			throw new AuthReqFailed_Exception("Parse ReqAuthResponse and decrypt/parse UserCred: " + e.getMessage(), new AuthReqFailed());
		}
		//Get Credentials
		byte[] sessionKey = user.getSessionKey();
		byte[] encryptionKey = user.getEncryptionKey();
		int server_nounce = user.getNounce();
		//Verify Nounce
		if(nounce != server_nounce)
			throw new AuthReqFailed_Exception("Nounces are differente!", new AuthReqFailed());
		
		//Save Credentials
		CredentialsManager cm = CredentialsManager.getInstance();
		cm.addCredentials(userId, bEncTicket, sessionKey, encryptionKey);
		
		byte[] bytes = new byte[1];
		Arrays.fill( bytes, (byte) 1 );
		return bytes;
	}

}