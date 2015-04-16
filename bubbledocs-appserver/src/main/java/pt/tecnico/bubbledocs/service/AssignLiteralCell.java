package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.domain.Content;
import pt.tecnico.bubbledocs.domain.LiteralContent;

import java.util.StringTokenizer;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.exception.SpreadSheetIdUnknown;
import pt.tecnico.bubbledocs.exception.UserHasNotWriteAccessException;
import pt.tecnico.bubbledocs.exception.PositionOutOfBoundsException;
import pt.tecnico.bubbledocs.exception.InvalidLiteralValueException;

import java.lang.NumberFormatException;

// add needed import declarations

public class AssignLiteralCell extends AccessBubbleDocsService {
	private String result;
	private String token;
	private int sheetId;
	private String cellCoords;
	private String literalValue;
	
	public AssignLiteralCell(String token, int docId, String cellId,String literal) {
	
		this.token=token;
		this.sheetId = docId;
		this.cellCoords = cellId;
		this.literalValue = literal;
		
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = BubbleDocs.getInstance();
		
		// First, we check the validity of the user.	
		validateUser(token);
		
		// After that, we get the spreadsheet and test if it's valid.
		SpreadSheet sheet = bd.getSpreadsheetById(sheetId);
		
		// Then we check if they have writing permissions.
		checkWritePermission(token, sheet);
		
		// Afterwards we grab the cell. For that, we need to parse.
		Cell cell = sheet.getCellFromString(cellCoords); // <-- Also throws a PositionOutOfBoundsException in case the spreadsheet doesn't have the provided values! 
		
		// Finally, we check the validity of the value.
		int value = 0;
		
		try{
			value = Integer.parseInt(literalValue);
		} catch (NumberFormatException e) {
			throw new InvalidLiteralValueException();
		}
		
		// And now we finally get to assign it! First we check if there's stuff there already...
		Content content = cell.getContent();
		if(content != null){
			content.delete();
			cell.setContent(null);
		}

		// ...and then we create the new content and assign it to the provided cell.
		LiteralContent resultingContent = new LiteralContent(value);
		cell.setContent(resultingContent);
		
		
		// Lastly, we assign our service the actual result of our operation!
		this.result = Integer.toString(value);
	
	}	

	public String getResult() {
		return result;
	}

}
