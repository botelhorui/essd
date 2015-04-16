package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.domain.Content;
import pt.tecnico.bubbledocs.domain.ReferenceContent;

import java.util.StringTokenizer;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.exception.SpreadSheetIdUnknown;
import pt.tecnico.bubbledocs.exception.UserHasNotWriteAccessException;
import pt.tecnico.bubbledocs.exception.PositionOutOfBoundsException;

// add needed import declarations

public class AssignReferenceCell extends AccessBubbleDocsService {
	private String result;
	private String token;
	private int iSpreadId;
	private String sCellId; // "1;1"
	private String sReference; // "1;1"

	public AssignReferenceCell(String token, int docId, String cellId, String reference){
		this.iSpreadId = docId;
		this.sCellId = cellId;
		this.sReference = reference;
		this.token=token;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = BubbleDocs.getInstance();
		

		//Fetch SpreadSheet object from BubbleDocs by ID
		SpreadSheet spread = bd.getSpreadsheetById(iSpreadId);

		String username = bd.getUsernameFromToken(token);
		User check = bd.getUserByUsername(username);
		
		// Session validations and renewals
		validateUser(token);

		//Fetch user and check if he has write permission for the SpreadSheet
		checkWritePermission(token, spread);


		//Fetch Cell from SpreadSheet
		Cell cell = spread.getCellFromString(sCellId);

		//Fetch Cell to Reference from SpreadSheet
		Cell reference = spread.getCellFromString(sReference);

		//Delete current Cell content if it exists
		Content content = cell.getContent();
		if(content != null){
			content.delete();
			cell.setContent(null);
		}

		//Add the new ReferenceContent
		ReferenceContent rc = new ReferenceContent(reference);
		cell.setContent(rc);

		//Transform the ReferenceContent to result String
		reference = null;
		rc = (ReferenceContent) cell.getContent();
		reference = rc.getReferenceCell();

		result = reference.getLine() + ";" + reference.getColumn();
	}

	public final String getResult() {
		return result;
	}
}