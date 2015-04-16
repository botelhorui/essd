package pt.tecnico.bubbledocs.service;

import org.jdom2.Document;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.SpreadSheetIdUnknown;
import pt.tecnico.bubbledocs.exception.UserHasNotReadAccessException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

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
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = BubbleDocs.getInstance();
		SpreadSheet s = bd.getSpreadsheetById(docId);
		
		validateUser(token);
		
		checkReadPermission(token, s);
		
		docXML = s.export();		
	}
}

