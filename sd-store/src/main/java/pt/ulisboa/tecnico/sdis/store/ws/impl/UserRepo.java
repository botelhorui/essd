package pt.ulisboa.tecnico.sdis.store.ws.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.handlers.Tag;



public class UserRepo {
	private int repoSize = 0;
	private Map<String, SDDoc> docs = new HashMap<String, SDDoc>();
	private String userId;

	public UserRepo(String userId) {
		this.userId=userId;
	}

	public void createDoc(String name) throws DocAlreadyExists_Exception{
		if(docs.containsKey(name)){
			throw new DocAlreadyExists_Exception("name "+name+" is already in use.", new DocAlreadyExists());
		} else {
			docs.put(name, new SDDoc());
		}
	}

	public void storeDoc(String name, byte[] content) throws CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception{		
		if(!docs.containsKey(name)){
			throw new DocDoesNotExist_Exception("doc with name '"+name+"' doesn't exist in "+userId+" repo.", new DocDoesNotExist());
		}
		docs.get(name).setData(content);		
	}

	public byte[] getDocument(String docId){
		return docs.get(docId).getData();
	}

	public List<String> listDocs(){
		return new ArrayList<String>(docs.keySet());
	}

	public byte[] loadDoc(String documentId) throws DocDoesNotExist_Exception {
		if(docs.containsKey(documentId)){
			return docs.get(documentId).getData();
		}else{
			throw new DocDoesNotExist_Exception("doc with name '"+documentId+"' doesn't exist in "+userId+" repo.", new DocDoesNotExist());
		}
	}
	
	public Tag getDocVersion(String documentId) throws DocDoesNotExist_Exception{
		if(docs.containsKey(documentId)){
			return docs.get(documentId).getVersion();
		}else{
			throw new DocDoesNotExist_Exception("doc with name '"+documentId+"' doesn't exist in "+userId+" repo.", new DocDoesNotExist());
		}	
	}
}
