package pt.ulisboa.tecnico.sdis.store.ws.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.*;

import pt.ulisboa.tecnico.sdis.store.ws.*; // classes generated from WSDL

@WebService(
    endpointInterface="pt.ulisboa.tecnico.sdis.store.ws.SDStore", 
    wsdlLocation="SD-STORE.1_1.wsdl",
    name = "SDStore",
    portName="SDStoreImplPort",
    targetNamespace="urn:pt:ulisboa:tecnico:sdis:store:ws",
    serviceName="SDStore"
)
public class SDStoreImpl implements SDStore {
	
	Map<String, UserRepo> userRepos = new HashMap<String, UserRepo>();
	

	public SDStoreImpl(){
		populateDomain();
	}
	
	private void populateDomain(){
		/*
		 * users:
		 * alice
		 * bruno
		 * carla
		 * duarte
		 * eduardo
		 */
	}
	
	
	
	@Override
	public void createDoc(DocUserPair docUserPair)
			throws DocAlreadyExists_Exception {
		
		if ( userRepos.containsKey( docUserPair.getUserId() )){
			
			userRepos.get(docUserPair.getUserId()).createDoc(docUserPair.getDocumentId());
		} else {
			
			UserRepo ur = new UserRepo(docUserPair.getUserId());
			ur.createDoc(docUserPair.getDocumentId());
			userRepos.put(docUserPair.getUserId(), ur);
			
		}
		
	}
	
	
	

	@Override
	public List<String> listDocs(String userId)
			throws UserDoesNotExist_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void store(DocUserPair docUserPair, byte[] contents)
			throws CapacityExceeded_Exception, DocDoesNotExist_Exception,
			UserDoesNotExist_Exception {
		
		if(userRepos.containsKey(docUserPair.getUserId())){
			
			userRepos.get(docUserPair.getUserId()).storeDoc(docUserPair.getDocumentId(), contents);
		} else {
			
			throw new UserDoesNotExist_Exception("o user "+docUserPair.getUserId()+" nao tem repo associado", null);
		}
		
	}
	
	

	@Override
	public byte[] load(DocUserPair docUserPair)
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	

}
