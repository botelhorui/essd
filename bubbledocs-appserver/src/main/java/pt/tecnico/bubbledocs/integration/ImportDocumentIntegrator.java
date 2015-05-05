package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;
import pt.tecnico.bubbledocs.service.ImportDocument;
import pt.tecnico.bubbledocs.service.GetUsername4TokenService;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;

import pt.tecnico.bubbledocs.domain.BubbleDocs;

//Tirar servicos remotos do dominio
//Possivelmente passar a validacao do user para o getUsername4Token()
//tirar cenas do construtor e passar para dispatch
//Criar ImportDocumentException
//criar package local

public class ImportDocumentIntegrator extends BubbleDocsIntegrator {
	
	private ImportDocument importService;
	private GetUsername4TokenService usernameService;
	private StoreRemoteServices storeService;
	
	private String _token;
	private String _spreadId;
	private byte[] _document;
	
	public ImportDocumentIntegrator(String token, int docId){
		_token = token;
		_spreadId = Integer.toString(docId);
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException {
		usernameService = new GetUsername4TokenService(_token);
		storeService = new StoreRemoteServices();
		
		//Gets username and validates user
		String username = usernameService.getUsername();
		
		try {	
			_document = storeService.loadDocument(username, _spreadId);
			
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();

		}
		
		importService = new ImportDocument(_token, _document);
		
		importService.execute();
	
	}	
}