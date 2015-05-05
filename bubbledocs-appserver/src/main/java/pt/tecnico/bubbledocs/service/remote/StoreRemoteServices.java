package pt.tecnico.bubbledocs.service.remote;

import pt.tecnico.bubbledocs.exception.CannotStoreDocumentException;
import pt.tecnico.bubbledocs.exception.CannotLoadDocumentException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.cli.SDStoreClient;

public class StoreRemoteServices {

	SDStoreClient storeclient;
	
	
	public void storeDocument(String username, String docName, byte[] document)
			throws CannotStoreDocumentException, RemoteInvocationException {
		
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId(docName);
		pair.setUserId(username);
		
			try {
				storeclient.store(pair, document);
			
			} catch (DocDoesNotExist_Exception e) {
				
				try {
					storeclient.createDoc(pair);
					storeclient.store(pair, document);
				} catch (DocAlreadyExists_Exception
						| CapacityExceeded_Exception
						| DocDoesNotExist_Exception
						| UserDoesNotExist_Exception e1) {
					throw new CannotStoreDocumentException();
				}
				
			} catch (CapacityExceeded_Exception | UserDoesNotExist_Exception e) {
				throw new CannotStoreDocumentException();
			}
		
	}
	
	public byte[] loadDocument(String username, String docName)
			throws CannotLoadDocumentException, RemoteInvocationException {
		byte[] document;
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId(docName);
		pair.setUserId(username);
		
		try {
			document = storeclient.load(pair);
		} catch (DocDoesNotExist_Exception | UserDoesNotExist_Exception e) {
			throw new CannotLoadDocumentException();
		}
		
		return document; 
	}

}
