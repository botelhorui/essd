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

public class TestClient {

	SDIdClient client;
	
	@Mocked 
	UDDINaming uddiNaming;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		new Expectations(){{
			new UDDINaming(anyString);
			uddiNaming.lookup(anyString); result="http://localhost:8080/id-ws/endpoint";
		}};
		
		client = new SDIdClient("http://localhost:8081", "SD-ID");
	}

	@After
	public void tearDown() throws Exception {
	
	}
    
}