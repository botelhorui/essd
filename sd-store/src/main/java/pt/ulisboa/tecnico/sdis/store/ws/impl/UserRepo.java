package pt.ulisboa.tecnico.sdis.store.ws.impl;


import java.util.HashMap;
import java.util.Map;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;



public class UserRepo {
	
	


	
	private int repoSize = 0, maxsize=10*1024;
	private Map<String, byte[]> docs = new HashMap<String, byte[]>();
	
	
	
	public UserRepo(String user) {
		super();
	}
	
	
	
	public void createDoc(String name) throws DocAlreadyExists_Exception{
		
		if(docs.containsKey(name)){
			
			throw new DocAlreadyExists_Exception("name "+name+" is already in use.", null);
			
		} else {
			
			docs.put(name, null);
			
		}
	}
	
	
	
	
	public void storeDoc(String name, byte[] content) throws CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception{
		
		int nsize;
		nsize = repoSize + content.length - docs.get(name).length;
		
		if(!docs.containsKey(name)){
			
			throw new DocDoesNotExist_Exception("name "+name+" doesn't exist in repo.", null);
			
		} else if (nsize > maxsize) {
			
			throw new DocDoesNotExist_Exception("repo exceeds permitted size", null);
		} else {
			
			docs.put(name, content);
			repoSize = nsize;
			
		}
	}





	public byte[] getDocument(String docId){
		return docs.get(docId);
	}



	
	
	

}
