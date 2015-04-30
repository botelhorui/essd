package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.AssignReferenceCell;


public class AssignReferenceCellIntegrator extends BubbleDocsIntegrator {
	
	private AssignReferenceCell service;
	
	public AssignReferenceCellIntegrator(String token, int docId, String cellId, String reference){
		
		service = new AssignReferenceCell(token, docId, cellId, reference);
		
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException {

		service.execute();
	
	}	
	
	public final String getResult() {

		return service.getResult();
		
	}

}
