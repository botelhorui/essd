package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

import org.jdom2.Document;
import java.io.IOException;
import org.jdom2.JDOMException;


public class ImportDocument extends AccessBubbleDocsService {
	private String _token;
	private int _spreadId;
	private byte[] _bXML;
	
	
	public ImportDocument(String token, int docId){
		this._token = token;
		this._spreadId = docId;
	}
	
	public void setByteXML(byte[] bXML){
		this._bXML = bXML;
	}
	
	public String getSpreadId(){
		String s = Integer.toString(_spreadId);
		return s;
	}
	
	public void validateUser(String token){
		super.validateUser(token);
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException{
		BubbleDocs bd = BubbleDocs.getInstance();
		Document JDOMdoc = null;
		try{
			JDOMdoc = bd.buildJDOMDocumentFromByteArray(_bXML);
		}catch(JDOMException e){
			
		}catch(IOException e){
			
		}
		
		String username = bd.getUsernameFromToken(_token);
		
		bd.importSheet(JDOMdoc, username);
		
	}

}