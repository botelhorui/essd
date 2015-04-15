	package pt.ulisboa.tecnico.sdis.store.ws.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.text.html.parser.DocumentParser;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.impl.SDStoreImpl;

public class SDStoreImplTest {

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
	public void createDocTest1() throws Exception {
		SDStore impl = new SDStoreImpl();
		DocUserPair p = new DocUserPair();
		p.setUserId("alice");
		p.setDocumentId("doc1");
		impl.createDoc(p);
		List<String> lst = impl.listDocs("alice");
		assertNotNull(lst);
		assertEquals("List of documents size should be just one", 1 , lst.size());
		assertEquals("The only document should have id 'doc1'","doc1", lst.get(0));
	}
	
	@Test
	public void createDocTest2() throws Exception {
		SDStore impl = new SDStoreImpl();
		DocUserPair p = new DocUserPair();
		p.setUserId("unknown");
		p.setDocumentId("doc1");
		impl.createDoc(p);
		List<String> lst = impl.listDocs("alice");
		assertNotNull(lst);
		assertEquals("List of documents size should be just one", 1 , lst.size());
		assertEquals("The only document should have id 'doc1'","doc1", lst.get(0));
	}
	
	@Test
	public void createDocTest3() throws Exception {
		SDStore impl = new SDStoreImpl();
		DocUserPair p = new DocUserPair();
		p.setUserId("alice");
		p.setDocumentId("doc1");
		impl.createDoc(p);
		try {
			impl.createDoc(p);
			fail("should throw exception");
		} catch (DocAlreadyExists_Exception e) {
			// success
		}		
	}
	
	@Test
	public void createDocTest4() throws Exception {
		SDStore impl = new SDStoreImpl();
		DocUserPair p = new DocUserPair();
		p.setUserId("alice");
		p.setDocumentId("doc1");
		impl.createDoc(p);
		p.setDocumentId("doc2");
		impl.createDoc(p);
		List<String> lst = impl.listDocs("alice");
		assertNotNull(lst);
		assertEquals("List of documents size should be just two", 2 , lst.size());
		assertTrue(lst.contains("doc1"));
		assertTrue(lst.contains("doc2"));
	}
	
	@Test
	public void listDocsTest1() throws Exception {
		SDStore impl = new SDStoreImpl();
		List<String> lst = impl.listDocs("alice");
		assertNotNull(lst);
		assertEquals("List of documents size should be just zero", 0 , lst.size());
	}

	@Test(expected=UserDoesNotExist_Exception.class)
	public void listDocsTest2() throws Exception {
		SDStore impl = new SDStoreImpl();
		List<String> lst = impl.listDocs("unknown");
	}

	@Test
	public void storeLoadTest1() throws Exception {
		SDStore impl = new SDStoreImpl();
		DocUserPair p = new DocUserPair();
		p.setUserId("alice");
		p.setDocumentId("doc1");
		impl.createDoc(p);
		byte[] data = new byte[1024];		
		new Random().nextBytes(data);
		impl.store(p, data);
		byte[] loaded = impl.load(p);
		assertTrue("The stored array is different from the loaded array.",Arrays.equals(data, loaded));
	}
	
	@Test(expected=UserDoesNotExist_Exception.class)
	public void storeLoadTest2() throws Exception {
		SDStore impl = new SDStoreImpl();
		DocUserPair p = new DocUserPair();
		p.setUserId("unkown");
		p.setDocumentId("doc1");
		byte[] data = new byte[1024];		
		new Random().nextBytes(data);
		impl.store(p, data);		
	}
	
	@Test(expected=DocDoesNotExist_Exception.class)
	public void storeLoadTest3() throws Exception {
		SDStore impl = new SDStoreImpl();
		DocUserPair p = new DocUserPair();
		p.setUserId("alice");
		p.setDocumentId("doc1");
		byte[] data = new byte[1024];		
		new Random().nextBytes(data);
		impl.store(p, data);		
	}
	
	@Test
	public void storeLoadTest4() throws Exception {
		SDStore impl = new SDStoreImpl();
		DocUserPair p = new DocUserPair();
		p.setUserId("alice");
		p.setDocumentId("doc1");
		impl.createDoc(p);
		byte[] data = new byte[10*1024];		
		new Random().nextBytes(data);
		impl.store(p, data);
		data = new byte[1];
		try {
			impl.store(p, data);
			fail("should throw CapacityExceeded_Exception");
		} catch (CapacityExceeded_Exception e) {
			// success
		}		
	}
	
	@Test(expected=DocDoesNotExist_Exception.class)
	public void LoadTest1() throws Exception {
		SDStore impl = new SDStoreImpl();
		DocUserPair p = new DocUserPair();
		p.setUserId("alice");
		p.setDocumentId("doc1");
		impl.load(p);		
	}
	
	@Test(expected=UserDoesNotExist_Exception.class)
	public void LoadTest2() throws Exception {
		SDStore impl = new SDStoreImpl();
		DocUserPair p = new DocUserPair();
		p.setUserId("unknown");
		p.setDocumentId("doc1");
		impl.load(p);		
	}
}
