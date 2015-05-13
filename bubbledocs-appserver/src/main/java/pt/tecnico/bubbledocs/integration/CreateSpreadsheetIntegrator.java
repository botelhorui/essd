package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.CreateSpreadSheet;

public class CreateSpreadsheetIntegrator extends BubbleDocsIntegrator {

	private CreateSpreadSheet service;
	private int sheetId;
	
	public CreateSpreadsheetIntegrator(String token, String name, int rows,int columns) {

		service = new CreateSpreadSheet(token, name, rows, columns);
		
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException {
		
		service.execute();
		sheetId=service.getSheetId();
	}	

	public int getSheetId(){
		return sheetId;
	}
}
