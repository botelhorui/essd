package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;
import pt.tecnico.bubbledocs.service.ImportDocument;
import pt.tecnico.bubbledocs.service.GetUsername4TokenService;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;

import pt.tecnico.bubbledocs.domain.BubbleDocs;


public class ImportDocumentIntegrator extends BubbleDocsIntegrator {
	
	private ImportDocument importService;
	private GetUsername4TokenService usernameService;
	
	private String _token;
	private String _spreadId;
	private byte[] _document;
	
	public ImportDocumentIntegrator(String token, int docId) {
		_token = token;
		usernameService = new GetUsername4TokenService(token);
		importService = new ImportDocument(token, docId);
		_spreadId = importService.getSpreadId();
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException {
		importService.validateUser(_token);
		
		BubbleDocs bd = BubbleDocs.getInstance();
		
		try {	
			_document = bd.StoreRemoteServices.loadDocument(usernameService.getUsername(), _spreadId);
			
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();

		}
		
		importService.setByteXML(_document);
		
		importService.execute();
	
	}	
}