package pt.tecnico.bubbledocs.exception;

public class ImportDocumentException extends BubbleDocsException {
	
	public ImportDocumentException(){
		
	}
	
	public ImportDocumentException(Throwable e){
		initCause(e);
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

}
