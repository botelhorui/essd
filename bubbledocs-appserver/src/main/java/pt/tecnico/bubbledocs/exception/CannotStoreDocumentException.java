package pt.tecnico.bubbledocs.exception;

public class CannotStoreDocumentException extends BubbleDocsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CannotStoreDocumentException(){
		
	}
	
	public CannotStoreDocumentException(Throwable e){
		initCause(e);
	}
}