package pt.tecnico.bubbledocs.service.remote;

import java.io.IOException;
import java.util.Properties;

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
	private static final String PROP_FILE = "remote-services.properties";
	SDStoreClient storeclient;
	
	public StoreRemoteServices() throws RemoteInvocationException{
		Properties props = new Properties();
		try {
			props.load(IDRemoteServices.class.getClassLoader().getResourceAsStream(PROP_FILE));
		} catch (IOException e1) {
			throw new RemoteInvocationException(e1);
		}
		try {
			String uddiUrl = props.getProperty("uddi.url");
			String storeName = props.getProperty("store.name");
			String storeClientId = props.getProperty("store.clientId");
			String N = props.getProperty("store.N");
			String RT = props.getProperty("store.RT");
			String WT = props.getProperty("store.WT");
			storeclient = new SDStoreClient(uddiUrl, storeName,Integer.parseInt(storeClientId) ,Integer.parseInt(N),Integer.parseInt(RT),Integer.parseInt(WT));
		} catch (SDStoreClientException | JAXRException e) {
			throw new RemoteInvocationException(e);			
		}
	}

	public static void main(String[] args) {
		new StoreRemoteServices();
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
			throw new CannotStoreDocumentException(e);
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
			throw new CannotLoadDocumentException(e);
		}catch (SDStoreClientException e){
			RemoteInvocationException t = new RemoteInvocationException(e);
			throw t;
		}

		return document; 
	}

}
