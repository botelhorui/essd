package pt.ulisboa.tecnico.sdis.store.ws.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;



public class UserRepo {
	private int repoSize = 0, maxsize=10*1024;
	private Map<String, byte[]> docs = new HashMap<String, byte[]>();
	private String userId;
	
	public UserRepo(String userId) {
		this.userId=userId;
	}

	public void createDoc(String name) throws DocAlreadyExists_Exception{

		if(docs.containsKey(name)){

			throw new DocAlreadyExists_Exception("name "+name+" is already in use.", null);

		} else {

			docs.put(name, new byte[0]);

		}
	}

	public void storeDoc(String name, byte[] content) throws CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception{
		
		if(!docs.containsKey(name)){
			throw new DocDoesNotExist_Exception("doc with name '"+name+"' doesn't exist in "+userId+" repo.", null);
		}
		int nsize = repoSize - docs.get(name).length + content.length ;
		
		if (nsize > maxsize) {
			throw new CapacityExceeded_Exception("New document data exceed repository capacity", null);
			
		} else {
			docs.put(name, content);
			repoSize = nsize;

		}
	}

	public byte[] getDocument(String docId){
		return docs.get(docId);
	}
	
	public List<String> listDocs(){
		return new ArrayList<String>(docs.keySet());
	}

	public byte[] loadDoc(String documentId) throws DocDoesNotExist_Exception {
		if(docs.containsKey(documentId)){
			return docs.get(documentId);
		}else{
			throw new DocDoesNotExist_Exception("doc with name '"+documentId+"' doesn't exist in "+userId+" repo.", null);
		}
	}
}
