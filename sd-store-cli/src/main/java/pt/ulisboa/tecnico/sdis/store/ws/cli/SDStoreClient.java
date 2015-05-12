package pt.ulisboa.tecnico.sdis.store.ws.cli;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.xml.registry.JAXRException;
import javax.xml.ws.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.AddressingFeature.Responses;

import example.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.store.ws.*; // classes generated from WSDL
import pt.ulisboa.tecnico.sdis.store.ws.handlers.VersionHandler;
import pt.ulisboa.tecnico.sdis.store.ws.handlers.Tag;

/**
 *  SDStore client.
 *
 *  Adds easier endpoint address configuration
 *  to the PortType generated by wsimport.
 *
 *  TODO Could also be extended to add UDDI lookup capability.
 */
public class SDStoreClient implements SDStore {

	/** WS service */
	private SDStore_Service service = null;
	private int N;
	private int RT;
	private int WT;
	private List<SDStore> ports = new ArrayList<SDStore>();
	private final int TIMEOUT = 5;
	private int clientId;

	public String decorate(String s){
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		Date date = new Date();
		return String.format("[%s][SDStoreClient]", format.format(date))+s;
	}
	
	public void println(String s){
		System.out.println(decorate(s));
	}
	
	public void printf(String s, Object... args){
		System.out.printf(decorate(s), args);
	}

	public SDStoreClient(String uddiURL, String wsName, int clientId, int N, int RT, int WT) throws SDStoreClientException, JAXRException {
		this.clientId = clientId;
		this.N=N;
		this.RT=RT;
		this.WT=WT;
		printf("Initialized:%n"
				+ "nuddiURL=%s%n"
				+ "wsName=%s%n"
				+ "clientId=%d%n"
				+ "N=%d%n"
				+ "RT=%d%n"
				+ "WT=%d%n",
				uddiURL,wsName,clientId,N,RT,WT);
		UDDINaming uddiNaming = new UDDINaming(uddiURL);
		Collection<String> endpoints = uddiNaming.queryServiceBindings(wsName);
		List<String> processedEndpoints = new ArrayList<String>();

		if (endpoints == null || endpoints.size() == 0) {
			throw new SDStoreClientException(String.format("Couldn't find '%s' Web Service at uddi",wsName));
		}
		printf("Got %d endpoint addresses:%n",endpoints.size());
		for(String endpoint: endpoints){
			if(processedEndpoints.contains(endpoint)){
				println("already has endpoint "+endpoint);
				continue;
			}else{
				println("does not have endpoint " + endpoint);
				processedEndpoints.add(endpoint);
			}
		}
		
		if(processedEndpoints.size()!=N){
			String err = String.format("Failed to get N=%d endpoint addresses for wsName=%s%n",N,wsName);
			printf(err);
			throw new SDStoreClientException(err);
		}
		
		for(String endpoint: processedEndpoints){
			service = new SDStore_Service();
			SDStore port = service.getSDStoreImplPort();
			BindingProvider bindingProvider = (BindingProvider) port;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
			ports.add(port);
		}
		printf("Result %d endpoint addresses%n",ports.size());
	}

	// SDStore webservice
	@Override
	public void createDoc(DocUserPair docUserPair)
			throws DocAlreadyExists_Exception {
		for(SDStore p: ports){
			p.createDoc(docUserPair); // WARNING this is imcomplete
		}
	}
	@Override
	public List<String> listDocs(String userId)
			throws UserDoesNotExist_Exception {
		List<String> l=null;
		for(SDStore p: ports){
			l = p.listDocs(userId);
			break;
		}
		return l;


	}
	@Override
	public void store(DocUserPair docUserPair, byte[] contents)
			throws CapacityExceeded_Exception,
			DocDoesNotExist_Exception,
			UserDoesNotExist_Exception {
		
		List<Response<LoadResponse>> loadResponses = new ArrayList<Response<LoadResponse>>();
		Map<Response<LoadResponse>,BindingProvider> loadResponseEndpoints = new HashMap<Response<LoadResponse>, BindingProvider>();

		for(SDStore p: ports){
			BindingProvider bp = (BindingProvider)p;
			Map<String,Object> rc = bp.getRequestContext();
			String address = (String)rc.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
			printf("requesting load async to %s%n",address);
			rc.put(VersionHandler.VERSION_QUERY_PROPERTY, VersionHandler.VERSION_QUERY_PROPERTY);
			Response<LoadResponse> r = p.loadAsync(docUserPair);			
			loadResponses.add(r);
			loadResponseEndpoints.put(r,bp);
		}
		Tag maxVersion=null;
		int completed = 0;
		long elapsed= 0;
		long start = System.nanoTime();
		double seconds = 0;
		try{
			
		}finally{
			
		}
		while(true){
			elapsed = start - System.nanoTime();
			seconds = (double)elapsed/1e9f;
			if(seconds > TIMEOUT){
				// TODO throw exception RemoteServiceException
				throw new SDStoreClientException("SDStoreClient getting maxVersion timeout");
			}
			for(Iterator<Response<LoadResponse>> it = loadResponses.iterator();it.hasNext();){
				Response<LoadResponse> r = it.next();				
				if(r.isDone()){
					
					it.remove();
					BindingProvider bp = loadResponseEndpoints.get(r);
					Map<String,Object> rc = bp.getRequestContext();
					String address = (String) rc.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
					Map<String,Object> resp = r.getContext();
					Tag version = (Tag)resp.get(VersionHandler.VERSION_PROPERTY);
					if(version == null){
						throw new SDStoreClientException("LoadResponse doesn't contain version tag");
					}				
					if(maxVersion==null){
						maxVersion=version;						
					}else if(version.greaterThan(maxVersion)){
						maxVersion=version;
					}
					printf("Received StoreResponse from %s. version value is %s%n",address,version);
					completed++;								
				}
			}

			if(completed>=RT){
				break;
			}	
		}
		printf("Determined that maxVersion=%s%n",maxVersion);
		maxVersion.setClientId(clientId);
		maxVersion.setSeqn(maxVersion.getSeqn()+1);
		List<Response<StoreResponse>> storeResponses = new ArrayList<Response<StoreResponse>>();
		Map<Response<StoreResponse>,BindingProvider> storeResponsesBindingProviders = new HashMap<Response<StoreResponse>, BindingProvider>();

		for(SDStore p: ports){
			BindingProvider bp = (BindingProvider)p;
			Map<String,Object> rc = bp.getRequestContext();
			String address = (String) rc.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
			printf("requesting storeAsync request to %s%n",address);
			rc.put(VersionHandler.VERSION_PROPERTY, maxVersion);
			Response<StoreResponse> r = p.storeAsync(docUserPair, contents);			
			storeResponses.add(r);
			storeResponsesBindingProviders.put(r, (BindingProvider)p);
		}

		start = System.nanoTime();
		while(true){
			elapsed = start - System.nanoTime();
			seconds = (double)elapsed/1e9f;
			if(seconds > TIMEOUT){
				// TODO throw exception RemoteServiceException
				throw new RuntimeException("SDStoreClient store timeout");
			}

			for(Iterator<Response<StoreResponse>> it = storeResponses.iterator();it.hasNext();){
				Response<StoreResponse> r = it.next();				
				if(r.isDone()){
					BindingProvider bp = storeResponsesBindingProviders.get(r);
					Map<String,Object> rc = bp.getRequestContext();
					String address = (String)rc.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
					printf("Received StoreResponse from %s%n",address);					
					it.remove();
					completed++;				
				}
			}
			if(completed>=WT){
				break;
			}
		}		
		println("Finished store call");
		
	}

	public String getEndpointAddress(BindingProvider bp){
		Map<String,Object> rc = bp.getRequestContext();
		String address = (String) rc.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);	
		return address;
	}

	public String getEndpointAddress(SDStore p){
		return getEndpointAddress((BindingProvider)p);
	}

	/*
	 * (non-Javadoc)
	 * @see pt.ulisboa.tecnico.sdis.store.ws.SDStore#load(pt.ulisboa.tecnico.sdis.store.ws.DocUserPair)
	 * Throw SDStoreClientException runtime exception when an operation timeouts
	 */
	@Override
	public byte[] load(DocUserPair docUserPair)
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception{		
		List<Response<LoadResponse>> loadResponses = new ArrayList<Response<LoadResponse>>();
		Map<Response<LoadResponse>,BindingProvider> loadResponsesBindingProviders = new HashMap<Response<LoadResponse>, BindingProvider>();
		/*
		 * Get all the value from the maximum version from RT replicas
		 */
		for(SDStore p: ports){
			BindingProvider bp = (BindingProvider)p;
			Map<String,Object> rc = bp.getRequestContext();
			String address = (String)rc.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
			if(rc.containsKey(VersionHandler.VERSION_QUERY_PROPERTY)){
				rc.remove(VersionHandler.VERSION_QUERY_PROPERTY);
			}
			printf("requesting load async to %s%n",address);			
			Response<LoadResponse> r = p.loadAsync(docUserPair);
			loadResponses.add(r);
			loadResponsesBindingProviders.put(r, bp);			
		}
		
		byte[] data = null;
		Tag maxVersion=null;		
		int completed = 0;
		long elapsed= 0;
		long start = System.nanoTime();
		double seconds = 0;
		while(true){
			elapsed = start - System.nanoTime();
			seconds = (double)elapsed/1e9f;
			if(seconds > TIMEOUT){
				// TODO throw exception RemoteServiceException
				throw new SDStoreClientException("Timeout of waiting for RT replicas to responde");
			}
			for(Iterator<Response<LoadResponse>> it = loadResponses.iterator();it.hasNext();){
				Response<LoadResponse> r = it.next();
				if(r.isDone()){					
					it.remove();	

					BindingProvider bp = loadResponsesBindingProviders.get(r);
					Map<String,Object> rc = bp.getRequestContext();
					String address = (String)rc.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);	
					Map<String,Object> resp = r.getContext();
					if(resp==null){
						try {
							r.get();
						} catch (Exception e) {							
							e.printStackTrace();
							throw new SDStoreClientException("Exception when getting LoadResponse contents:"+e.getCause().toString()); 
						}
						throw new SDStoreClientException("Failed to get response context");
					}
					Tag version = (Tag)resp.get(VersionHandler.VERSION_PROPERTY);
					if(version == null){
						throw new SDStoreClientException("LoadResponse doesn't contain version tag");
					}
					
					byte[] temp = null;
					try {
						temp = r.get().getContents();						
					} catch (InterruptedException | ExecutionException e) {	
						println("Caught exception when trying to get Response's future content%nCause:%n"+e.getCause());
						throw new SDStoreClientException("Exception when getting LoadResponse contents"); 
					}
					printf("Received LoadResponse from %s. version value is %s. contents is %s bytes%n",
							address,version,temp == null?null:Integer.toString(temp.length));					
					if(maxVersion==null){
						maxVersion=version;
						data = temp;
					}else if(version.greaterThan(maxVersion)){
						maxVersion=version;
						data = temp;
					}
					completed++;
				}
			}
			if(completed>=RT){
				break;
			}

		}
		
		// Write-back
		List<Response<StoreResponse>> storeResponses = new ArrayList<Response<StoreResponse>>();
		Map<Response<StoreResponse>,BindingProvider> storeResponsesBindingProviders = new HashMap<Response<StoreResponse>, BindingProvider>();

		println("Starting write back");
		for(SDStore p: ports){
			BindingProvider bp = (BindingProvider)p;
			Map<String,Object> rc = bp.getRequestContext();
			String address = (String)rc.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
			printf("requesting write async to %s%n",address);

			rc.put(VersionHandler.VERSION_PROPERTY, maxVersion);
			Response<StoreResponse> r = p.storeAsync(docUserPair,data);
			storeResponses.add(r);
			storeResponsesBindingProviders.put(r, (BindingProvider)p);			
		}				

		start = System.nanoTime();
		while(true){
			elapsed = start - System.nanoTime();
			seconds = (double)elapsed/1e9f;
			if(seconds > TIMEOUT){
				// TODO throw exception RemoteServiceException
				throw new SDStoreClientException("SDStoreClient write-back timeout");
			}
			for(Iterator<Response<StoreResponse>> it = storeResponses.iterator();it.hasNext();){
				Response<StoreResponse> r = it.next();				
				if(r.isDone()){
					
										
					BindingProvider bp = loadResponsesBindingProviders.get(r);
					Map<String,Object> rc = bp.getRequestContext();
					String address = (String)rc.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
					printf("Received StoreResponse from %s%n",address);
					it.remove();
					completed++;
				}
			}
			if(completed>=WT){
				break;
			}
		}

		println("Write-back complete");	
		
		return data;
	}

	@Override
	public Response<CreateDocResponse> createDocAsync(DocUserPair docUserPair) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<?> createDocAsync(DocUserPair docUserPair,
			AsyncHandler<CreateDocResponse> asyncHandler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<ListDocsResponse> listDocsAsync(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<?> listDocsAsync(String userId,
			AsyncHandler<ListDocsResponse> asyncHandler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<StoreResponse> storeAsync(DocUserPair docUserPair,
			byte[] contents) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<?> storeAsync(DocUserPair docUserPair, byte[] contents,
			AsyncHandler<StoreResponse> asyncHandler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<LoadResponse> loadAsync(DocUserPair docUserPair) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<?> loadAsync(DocUserPair docUserPair,
			AsyncHandler<LoadResponse> asyncHandler) {
		// TODO Auto-generated method stub
		return null;
	}

}
