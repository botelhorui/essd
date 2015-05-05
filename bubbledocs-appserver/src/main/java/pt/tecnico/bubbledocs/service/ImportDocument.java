package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.ImportDocumentException;

import org.jdom2.Document;
import java.io.IOException;
import org.jdom2.JDOMException;


public class ImportDocument extends AccessBubbleDocsService {
	private String _token;
	private byte[] _bXML;
	
	
	public ImportDocument(String token, byte[] bXML){
		this._token = token;
		this._bXML = bXML;
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException{
		BubbleDocs bd = BubbleDocs.getInstance();
		Document JDOMdoc = null;
		try{
			JDOMdoc = bd.buildJDOMDocumentFromByteArray(_bXML);
		}catch(JDOMException e){
			throw new ImportDocumentException();
		}catch(IOException e){
			throw new ImportDocumentException();
		}
		
		String username = bd.getUsernameFromToken(_token);
		
		bd.importSheet(JDOMdoc, username);
		
	}

}