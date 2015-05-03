package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.GetSpreadSheetContent;


public class GetSpreadSheetContentIntegrator extends BubbleDocsIntegrator {
	
	private GetSpreadSheetContent service;  
	
	public GetSpreadSheetContentIntegrator(String token, int docId) {
		
		service = new GetSpreadSheetContent(token, docId);
		
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException {
		
		service.execute();
	
	}	

	public final String[][] getResult() {

		return service.getResult();
		
	}
}