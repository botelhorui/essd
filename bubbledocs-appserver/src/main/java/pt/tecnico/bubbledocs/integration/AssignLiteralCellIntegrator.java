package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.AssignLiteralCell;


public class AssignLiteralCellIntegrator extends BubbleDocsIntegrator {
	
	private AssignLiteralCell service;  
	
	public AssignLiteralCellIntegrator(String token, int docId, String cellId, String literal) {
		
		service = new AssignLiteralCell(token, docId, cellId, literal);
		
	}
	
	protected void dispatch() throws BubbleDocsException {
		
		service.execute();
	
	}	

	public final String getResult() {

		return service.getResult();
		
	}
}
