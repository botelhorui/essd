package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.service.ExportDocument;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;
import pt.tecnico.bubbledocs.service.GetUsername4TokenService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.jdom2.Document;

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
		storeService = new StoreRemoteServices();

		//NAO PODE HAVER METODOS DO DOMINIO
		//SpreadSheet s = bd.getSpreadsheetById(docId);

		// ???
		// not sure if this is still supposed to be used in the remote call
		/*validateUser(token);
		checkReadPermission(token, s);*/
		
		//Gets username and validates user
		String username = usernameService.getUsername();
		
		try {
			//Spread s can't exist here - no domain objects!
			//storeService.storeDocument(username, null, s.spreadtoBytes());

		} catch (Exception e) {

			throw new UnavailableServiceException();

		}

		service.execute();
	}
	
	public Document getDocXML() {
		return service.getDocXML();
	}

}
