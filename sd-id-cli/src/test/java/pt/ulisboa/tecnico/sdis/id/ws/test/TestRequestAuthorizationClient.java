package pt.ulisboa.tecnico.sdis.id.ws.test;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.*; // classes generated from WSDL
import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.cli.SDIdClient;
import uddi.UDDINaming;
import javax.xml.ws.WebServiceException;
import javax.xml.registry.JAXRException;

public class TestRequestAuthorizationClient extends TestClient {

	private static final String USERNAME = "carmina";
	private static final String NEW_EMAIL = "carmina@tecnico.pt";
	
	// Case 1: 
	//		the username doesn't exist
	@Test(expected=AuthReqFailed_Exception.class)
	public void authenticationFailedUserInexistent() throws Exception {
		
		String password = "1234567";
		byte[] to_bytes = password.getBytes();
		
		client.requestAuthentication( USERNAME , to_bytes );

	}

	// Case 2: 
	//		the username exists 
	//		the password doesn't match the stored
	@Test(expected=AuthReqFailed_Exception.class)
	public void authenticationPasswordDoesntMatch() throws Exception {

		//client.createUser( USERNAME , NEW_EMAIL );
		
		String password = "1234567";
		byte[] to_bytes = password.getBytes();

		client.requestAuthentication( USERNAME , to_bytes );

	}
}