package pt.tecnico.bubbledocs.exception;

import pt.ulisboa.tecnico.sdis.store.ws.cli.SDStoreClientException;

public class RemoteInvocationException extends BubbleDocsException {

	public RemoteInvocationException(String cause){
		super(cause);
	}
	public RemoteInvocationException(){
		
	}
	public RemoteInvocationException(Throwable e) {
		initCause(e);		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}