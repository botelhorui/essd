package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.ImportDocumentException;

import org.jdom2.Document;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


public class ImportDocument extends AccessBubbleDocsService {
	private String _token;
	private byte[] _bXML;
	private int _docId;
	
	
	public ImportDocument(String token, byte[] bXML){
		this._token = token;
		this._bXML = bXML;
	}
	
	public Document buildJDOMDocumentFromByteArray(byte[] bXML) throws JDOMException, IOException{
		SAXBuilder builder = new SAXBuilder();
		InputStream stream = new ByteArrayInputStream(bXML);
		Document JDOMdoc = builder.build(stream);

		return JDOMdoc;
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException{
		BubbleDocs bd = BubbleDocs.getInstance();
		Document JDOMdoc = null;
		
		validateUser(this._token);
		try{
			JDOMdoc = buildJDOMDocumentFromByteArray(_bXML);
		}catch(JDOMException e){
			throw new ImportDocumentException(e);
		}catch(IOException e){
			throw new ImportDocumentException();
		}
		
		String username = bd.getUsernameFromToken(_token);
		
		SpreadSheet spread = bd.importSheet(JDOMdoc, username);
		_docId = spread.getId();
		
	}

	public int get_docId() {
		return _docId;
	}

	
	

}