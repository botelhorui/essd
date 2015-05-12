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

public class TestSDIdClient {

	private static final String USERNAME = "USERNAME";
	private static final String EMAIL = "EMAIL";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	

	/**
     *  In this test the UDDI is not running in the expected address
     */
    @Test(expected=JAXRException.class)
    public void UDDIException( 
    		@Mocked final UDDINaming uddiNaming,
    		@Mocked final SDId_Service service,
    		@Mocked final SDId port)
        throws Exception {
    	
        new Expectations() {{
        	
        	new UDDINaming(anyString);
            uddiNaming.lookup(anyString); result = new JAXRException();
            
        }};

        // Unit under test is exercised.
        SDIdClient client = new SDIdClient("http://localhost:8081", "SD-ID");
        // call to mocked server
        client.createUser( USERNAME , EMAIL );
    }
    
}