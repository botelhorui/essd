package pt.tecnico.bubbledocs.integration;

import org.jdom2.Document;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.ExportDocument;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;
import pt.tecnico.bubbledocs.service.GetUsername4TokenService;

public class ExportDocumentIntegrator extends BubbleDocsIntegrator {

	private ExportDocument service;
	private int docId;
	private String _token;
	private StoreRemoteServices storeService;
	private GetUsername4TokenService usernameService;

	public ExportDocumentIntegrator(String token, int docId) {

		service = new ExportDocument(token,docId);
		this.docId = docId;
		this._token = token;

	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
		usernameService = new GetUsername4TokenService(_token);
		usernameService.execute();
		storeService = new StoreRemoteServices();

		byte[] s = service.getSpreadsheetBytesById(docId);

		// ???
		// not sure if this is still supposed to be used in the remote call
		/*validateUser(token);
		checkReadPermission(token, s);*/
		
		//Gets username and validates user
		
		String username = usernameService.getUsername();
		
		try {
			
			storeService.storeDocument(username, Integer.toString(docId), s);

		} catch (RemoteInvocationException e) {

			throw new UnavailableServiceException();

		}

		service.execute();
	}
	
	public Document getDocXML() {
		
		return service.getDocXML();
	
	}

}
