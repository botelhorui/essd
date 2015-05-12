package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Content;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class GetSpreadSheetContent extends AccessBubbleDocsService {
	private String _token;
	private int _spreadId;
	private String[][] _result;
	
	
	public GetSpreadSheetContent(String token, int docId){
		this._token = token;
		this._spreadId = docId;
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = BubbleDocs.getInstance();
		
		// First, we check the validity of the user.	
		validateUser(_token);
		
		// After that, we get the spreadsheet and test if it's valid.
		SpreadSheet sheet = bd.getSpreadsheetById(_spreadId);
		
		// Then we check if the user has reading permissions.
		checkReadPermission(_token, sheet);
		
		// We grab the size of the matrix / spreadsheet
		int lines = sheet.getLines();
		int columns = sheet.getColumns();
		
		//We create the matrix
		this._result = new String[lines+1][columns+1];
		
		// Lastly, we fill the matrix with the values of every cell with content
		fillMatrix(sheet, lines, columns);
	
	}
	
	public String[][] getResult(){
		return this._result;
	}
	
	public void fillMatrix(SpreadSheet sheet, int lines, int columns){
		Cell c;
		Content t;
		
		for(int i=1; i<=lines; i++){
			for(int j=1; j<=columns; j++){
				c = sheet.getCell(i,j);
				if(c != null){
					t = c.getContent();
					if(t != null){
						this._result[i][j] = t.returnValueAsString();
					}else{
						this._result[i][j] = "";
					}
				}else{
					this._result[i][j] = "";	
				}
			}
		}
	
	}

}