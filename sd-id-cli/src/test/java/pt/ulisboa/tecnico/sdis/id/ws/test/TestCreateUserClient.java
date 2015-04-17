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

public class TestCreateUserClient extends TestClient {

	private static final String USERNAME = "alice";
	
	private static final String NEW_USERNAME_1 = "francisco";
	private static final String NEW_USERNAME_2 = "guilherme";
	
	private static final String INVALID_USERNAME = "";
	private static final String NULL_USERNAME = null;

	private static final String EMAIL = "alice@tecnico.pt";
	
	private static final String NEW_EMAIL_1 = "francisco@tecnico.pt";
	private static final String NEW_EMAIL_2 = "guilherme@tecnico.pt";
	private static final String NEW_EMAIL_3 = "helena@tecnico.pt";
	
	private static final String INVALID_EMAIL = "";

	// Case 1: 
	//		email already exists
	@Test(expected=EmailAlreadyExists_Exception.class)
	public void emailAlreadyExists() throws Exception {
		
		client.createUser( NEW_USERNAME_1 , NEW_EMAIL_1 );

	}

	// Case 2: 
	//		invalid email
	@Test(expected=InvalidEmail_Exception.class)
	public void invalidEmail() throws Exception {
		
		client.createUser( NEW_USERNAME_2 , INVALID_EMAIL );

	}

	// Case 3: 
	//		email already exists
	@Test(expected=InvalidUser_Exception.class)
	public void invalidUser() throws Exception {

		client.createUser( NULL_USERNAME , NEW_EMAIL_2 );

	}

	// Case 4: 
	//		email already exists
	@Test(expected=UserAlreadyExists_Exception.class)
	public void userAlreadyExists() throws Exception {

		client.createUser( USERNAME , NEW_EMAIL_3 );

	}
}