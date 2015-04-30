package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.service.ExportDocument;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.jdom2.Document;

public class ExportDocumentIntegrator extends BubbleDocsIntegrator {

	private ExportDocument service;
	private int docId;
	private String token;

	public ExportDocumentIntegrator(String token, int docId) {

		service = new ExportDocument(token,docId);
		this.docId = docId;
		this.token = token;

	}

	@Override
	protected void dispatch() throws BubbleDocsException {

		BubbleDocs bd = BubbleDocs.getInstance();

		SpreadSheet s = bd.getSpreadsheetById(docId);

		// ???
		// not sure if this is still supposed to be used in the remote call
		/*validateUser(token);
		checkReadPermission(token, s);*/
		String username = bd.getUsernameFromToken(token);
		//
		
		try {

			bd.StoreRemoteServices.storeDocument(username, null, s.spreadtoBytes());

		} catch (Exception e) {

			throw new UnavailableServiceException();

		}

		service.execute();
	}	

}
