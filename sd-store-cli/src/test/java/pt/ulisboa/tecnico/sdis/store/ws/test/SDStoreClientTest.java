package pt.ulisboa.tecnico.sdis.store.ws.test;

import static org.junit.Assert.*;

import java.util.List;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;

import pt.ulisboa.tecnico.sdis.store.ws.cli.SDStoreClient;
import uddi.UDDINaming;


import javax.xml.ws.WebServiceException;

public class SDStoreClientTest {

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

	
	@Test
    public void createDocTest(
        @Mocked final UDDINaming uddiNaming)
        throws Exception {
		
		/*
		new Expectations() {{
            new UDDINaming(anyString);
            uddiNaming.lookup(anyString); result ="http://localhost:8080/store-ws/endpoint";
        }};
		
        */
		
		SDStoreClient impl = new SDStoreClient("http://localhost:8081", "SD-Store");
		
		DocUserPair p = new DocUserPair();
		p.setUserId("alice");
		p.setDocumentId("doc1");
		impl.createDoc(p);
		List<String> lst = impl.listDocs("alice");
		assertNotNull("null list", lst);
		assertEquals("List of documents size should be just one", 1 , lst.size());
		assertEquals("The only document should have id 'doc1'","doc1", lst.get(0));
	}
	
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@Test
	public void UDDIDown(){
		// TODO
		
	}
	
	@Test
	public void UDDILookupNull(){
		// TODO
	}
	
	

}
