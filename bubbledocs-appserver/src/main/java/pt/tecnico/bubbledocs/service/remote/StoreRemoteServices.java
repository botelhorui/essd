package pt.tecnico.bubbledocs.service.remote;

import javax.xml.registry.JAXRException;

import pt.tecnico.bubbledocs.exception.CannotStoreDocumentException;
import pt.tecnico.bubbledocs.exception.CannotLoadDocumentException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.cli.SDStoreClient;
import pt.ulisboa.tecnico.sdis.store.ws.cli.SDStoreClientException;

public class StoreRemoteServices {

	SDStoreClient storeclient;
	
	public StoreRemoteServices() throws SDStoreClientException{
		super();
		try {
			storeclient = new SDStoreClient("http://localhost:8081", "SD-Store",2 ,3,2,2);
		} catch (JAXRException e) {
			throw new SDStoreClientException();
			
		}
	}

	public void storeDocument(String username, String docName, byte[] document)
			throws CannotStoreDocumentException, RemoteInvocationException {

		DocUserPair pair = new DocUserPair();
		pair.setDocumentId(docName);
		pair.setUserId(username);

		try{
			try {
				storeclient.createDoc(pair);
			} catch (DocAlreadyExists_Exception e) {
				//if it already exists even better
			}
			storeclient.store(pair, document);
		}catch (CapacityExceeded_Exception | DocDoesNotExist_Exception
				| UserDoesNotExist_Exception e) {
			e.printStackTrace();
		}catch (SDStoreClientException e){
			RemoteInvocationException t = new RemoteInvocationException(e);
			throw t;
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
