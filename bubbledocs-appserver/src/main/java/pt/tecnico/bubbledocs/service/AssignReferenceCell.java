package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.ReferenceContent;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

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

		// Session validations and renewals
		validateUser(token);

		//Fetch user and check if he has write permission for the SpreadSheet
		checkWritePermission(token, spread);

		//Fetch Cell from SpreadSheet
		Cell cell = spread.getCellFromString(sCellId);

		//Fetch Cell to Reference from SpreadSheet
		Cell reference = spread.getCellFromString(sReference);

		//Add the new ReferenceContent
		cell.setReferenceContent(reference);
		
		ReferenceContent referenceCell = (ReferenceContent)cell.getContent();

		//Transform the ReferenceContent to result String
		result = referenceCell.returnReferencedCell();
	}

	public final String getResult() {
		return result;
	}
}