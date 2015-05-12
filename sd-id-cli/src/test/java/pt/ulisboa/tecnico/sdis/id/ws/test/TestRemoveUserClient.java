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
import example.ws.uddi.UDDINaming;
import javax.xml.ws.WebServiceException;
import javax.xml.registry.JAXRException;

public class TestRemoveUserClient extends TestClient {

	private static final String INEXISTENT_USERNAME = "aurelia";
	private static final String NEW_EMAIL = "aurelia@tecnico.pt";
	
	// Case 1: 
	//		the new user doesn't exist
	@Test(expected=UserDoesNotExist_Exception.class)
	public void userDoesNotExist() throws Exception {

		client.removeUser( INEXISTENT_USERNAME );

	}


	// Case 2: 
	//		the user was already removed
	@Test(expected=UserDoesNotExist_Exception.class)
	public void userTwiceRemoved() throws Exception {

		client.createUser( INEXISTENT_USERNAME , NEW_EMAIL);
		client.removeUser( INEXISTENT_USERNAME );
		client.removeUser( INEXISTENT_USERNAME );

	}

}