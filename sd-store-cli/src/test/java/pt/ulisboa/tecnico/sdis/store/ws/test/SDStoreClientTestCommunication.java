package pt.ulisboa.tecnico.sdis.store.ws.test;

import javax.xml.registry.JAXRException;
import javax.xml.ws.WebServiceException;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import pt.ulisboa.tecnico.sdis.store.ws.cli.SDStoreClient;
import uddi.UDDINaming;

public class SDStoreClientTestCommunication {
	
	
	
	@Mocked UDDINaming uddiNaming;
	DocUserPair p;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	

	@Before
	public void setUp() throws Exception {
		
		p = new DocUserPair();
		p.setUserId("alice");
		p.setDocumentId("doc3");
				
		
	}
	
	

	@After
	public void tearDown() throws Exception {
	}

	
	@Test(expected=JAXRException.class)
	public void uddiServerDown() throws Exception {
		
		new Expectations(){{
			new UDDINaming(anyString);
			uddiNaming.lookup(anyString); result= new JAXRException();
		}};
		
		SDStoreClient stuff = new SDStoreClient("http://localhost:8081", "SD-Store");
		stuff.load(p);		
	}
	
	
	
	@Test(expected=WebServiceException.class)
	public void portWebException(@Mocked SDStore port, @Mocked SDStore_Service store) throws Exception {
		
		new Expectations(){{
			
			new UDDINaming(anyString);
			uddiNaming.lookup(anyString); result="http://localhost:8080/store-ws/endpoint";
			
			new SDStore_Service();
			
			store.getSDStoreImplPort(); result = port;
			port.load(p); result = new WebServiceException();
			
		}};
		
		SDStoreClient client = new SDStoreClient("http://localhost:8081", "SD-Store");
		client.load(p);		
	}
	
	

}
