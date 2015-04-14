package pt.ulisboa.tecnico.sdis.store.ws.impl;

import java.util.List;

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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] load(DocUserPair docUserPair)
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
