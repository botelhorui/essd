package pt.tecnico.bubbledocs.exception;

public class CannotLoadDocumentException extends BubbleDocsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CannotLoadDocumentException(){
		
	}
	
	public CannotLoadDocumentException(Throwable e){
		initCause(e);
	}
}