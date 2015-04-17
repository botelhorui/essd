package pt.tecnico.bubbledocs.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.jdom2.Document;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.SpreadSheetIdUnknown;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserHasNotReadAccessException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

// add needed import declarations

public class ExportDocument extends AccessBubbleDocsService {
	private Document docXML;
	private String token;
	private int docId;

	public Document getDocXML() {
		return docXML;
	}

	public ExportDocument(String token, int docId) {
		// add code here
		this.token=token;
		this.docId=docId;
	}

	@Override
	protected void dispatch() throws BubbleDocsException, UnavailableServiceException {
		
		BubbleDocs bd = BubbleDocs.getInstance();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		
		try {
			out = new ObjectOutputStream(bos);
		} catch (IOException e2) {
			
			e2.printStackTrace();
		}
		
		StoreRemoteServices remote = new StoreRemoteServices();
		String username = bd.getUsernameFromToken(token);
		SpreadSheet s = bd.getSpreadsheetById(docId);
		
		try {
			out.writeObject(s);
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		
		byte[] doc = bos.toByteArray();
		
		validateUser(token);
		
		checkReadPermission(token, s);
		
		docXML = s.export();		
		
		try {
			remote.storeDocument(username, null, doc);
		} catch (Exception e) {
			
			throw new UnavailableServiceException();
		}
		
	}
}

