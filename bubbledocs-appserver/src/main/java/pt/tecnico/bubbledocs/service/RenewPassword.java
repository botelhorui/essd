package pt.tecnico.bubbledocs.service;

public class RenewPassword extends BubbleDocsService {
	private String _token;
	
	public RenewPassword(String token) {
		this._token = token;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = BubbleDocs.getInstance();
		User user = bd.getUserByToken(this._token);
		
		//TODO: Add BubbleDocsService method that verifies if user is in session
		
		try {
			//remote renew password
			bd.IDRemoteServices.renewPassword(user);
			user.setPassword(null);
			
		} catch (RemoteInvocationException e) {
			throws new UnavailableServiceException();
		}
		
		bd.renewSessionDuration(user);
		bd.renewToken(user);
		this._token = user.getSession().getToken();
		
	}

}