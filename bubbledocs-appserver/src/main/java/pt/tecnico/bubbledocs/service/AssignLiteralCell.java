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

public class AssignLiteralCell extends BubbleDocsService {
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
		
		String username = bd.getUsernameFromToken(token);  //<-- This method will need a rework for future checkpoints, I believe.
		bd.getUserByUsername(username); // <-- That method by itself throws the UnknownBubbleDocsUserException; don't have to worry about it here.
		
		// Checking if they're still in session; if they are, renew their validity.
		if(!bd.isUserInSession(token)){
			throw new UserNotInSessionException();
		}else{
			bd.renewSessionDuration(token);
		}
		
		// After that, we get the spreadsheet and test if it's valid.
		SpreadSheet sheet = null;
		
		for(SpreadSheet s: bd.getSpreadSheetSet()){
			if(s.getId() == sheetId){
				sheet = s;
				break;
			}
		}
		
		if (sheet == null){
			throw new SpreadSheetIdUnknown();
		}
		
		// Then we check if they have writing permissions.
		User writer = bd.getUserByToken(token);

		if(writer.checkWriteAccess(sheet) == false){
			throw new UserHasNotWriteAccessException();
		}
		
		// Afterwards we grab the cell. For that, we need to parse.
		StringTokenizer st = new StringTokenizer(cellCoords, ";", false);
		int buffer = 0;
		int cellRow = 0;
		int cellColumn = 0;
		
		if (st.countTokens() != 2){
			throw new PositionOutOfBoundsException();
		}

		while (st.hasMoreTokens()){
			try{
				buffer = Integer.parseInt(st.nextToken());
			} catch (NumberFormatException e) {
				throw new PositionOutOfBoundsException();
			}
			if(st.countTokens() == 1)
				cellRow = buffer;
			if(st.countTokens() == 0)
				cellColumn = buffer;
			
		}

		Cell cell = sheet.getCell(cellRow, cellColumn); // <-- Also throws a PositionOutOfBoundsException in case the spreadsheet doesn't have the provided values! 
		
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
