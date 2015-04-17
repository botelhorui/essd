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

public class TestRenewPasswordClient extends TestClient {

	private static final String INEXISTENT_USERNAME = "botelho";
	private static final String NEW_EMAIL = "botelhos@tecnico.pt";
	
	// Case 1: 
	//		the user does not exist
	@Test(expected=UserDoesNotExist_Exception.class)
	public void userDoesNotExist() throws Exception {

		client.createUser( INEXISTENT_USERNAME , NEW_EMAIL);
		client.removeUser( INEXISTENT_USERNAME );

		client.renewPassword( INEXISTENT_USERNAME );

	}
}