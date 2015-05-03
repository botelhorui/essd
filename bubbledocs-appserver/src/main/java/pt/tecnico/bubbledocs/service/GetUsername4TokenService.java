package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class GetUsername4TokenService extends BubbleDocsService{
	private String _token, _username;
	
	public GetUsername4TokenService(String token) {
		this._token = token;
		_username = null;
	}
	
	@Override
    protected void dispatch() throws BubbleDocsException{
		BubbleDocs bd = BubbleDocs.getInstance();
		
		_username = bd.getUsernameFromToken(_token);
    }
	
	public String getUsername() {
		return _username;
	}
    
}