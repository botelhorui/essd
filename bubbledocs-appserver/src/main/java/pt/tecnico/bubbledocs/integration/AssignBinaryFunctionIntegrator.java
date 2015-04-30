package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.service.AssignBinaryFunctionCell;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class AssignBinaryFunctionIntegrator extends BubbleDocsIntegrator {
	
	private AssignBinaryFunctionCell service;
	
	public AssignBinaryFunctionIntegrator(String token, int docId, String cellId, String expression) {
		
		service = new AssignBinaryFunctionCell(token, docId, cellId, expression);
		
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException {
		
		service.execute();
		
	}	

	public final String getResult() {

		return service.getResult();
		
	}
}
