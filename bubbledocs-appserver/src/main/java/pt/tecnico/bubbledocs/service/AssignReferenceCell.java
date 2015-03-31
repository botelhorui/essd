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
import pt.tecnico.bubbledocs.exception.UnknownBubbleDocsUserException;

// add needed import declarations

public class AssignReferenceCell extends BubbleDocsService {
	private String result;
	private String token;
	private int iSpreadId;
	private String sCellId; // "1;1"
	private String sReference;

	public AssignReferenceCell(String token, int docId, String cellId, String reference){
		this.iSpreadId = docId;
		this.sCellId = cellId;
		this.sReference = reference;
		this.token=token;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = BubbleDocs.getInstance();
		SpreadSheet spread = null;

		//Fetch SpreadSheet object from BubbleDocs by ID
		for(SpreadSheet s: bd.getSpreadSheetSet()){
			if(s.getId() == iSpreadId){
				spread = s;
				break;
			}
		}

		if(spread == null){
			throw new SpreadSheetIdUnknown();
		}

		String username = bd.getUsernameFromToken(token);
		User check = bd.getUserByUsername(username);
		
		// Session validations and renewals
		if(!bd.isUserInSession(token)){
			throw new UserNotInSessionException();
		}else{
			bd.renewSessionDuration(token);
		}

		//Fetch user and check if he has write permission for the SpreadSheet
		User u = bd.getUserByToken(token);

		if(u.checkWriteAccess(spread) == false){
			throw new UserHasNotWriteAccessException();
		}


		//Fetch Cell from SpreadSheet
		StringTokenizer st = new StringTokenizer(sCellId, ";", false);
		int i = 0;
		int row = 0;
		int column = 0;

		while (st.hasMoreTokens()){
			i = Integer.parseInt(st.nextToken());
			if(st.countTokens() == 1)
				row = i;
			if(st.countTokens() == 0)
				column = i;
		}

		Cell cell = spread.getCell(row, column);

		//Fetch Cell to Reference from SpreadSheet
		st = new StringTokenizer(sReference, ";", false);

		while (st.hasMoreTokens()){
			i = Integer.parseInt(st.nextToken());
			if(st.countTokens() == 1)
				row = i;
			if(st.countTokens() == 0)
				column = i;
		}

		Cell reference = spread.getCell(row, column);

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