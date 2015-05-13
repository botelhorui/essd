package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

// add needed import declarations

public class CreateSpreadSheet extends LoggedBubbleDocsService {
	private int sheetId, rows, columns;  // id of the new sheet
	private String token, name;
	private User owner;
	private SpreadSheet sp;

	public int getSheetId() {
		return sheetId;
	}
	
	public SpreadSheet getSpreadSheet(){
		return sp;
	}

	public CreateSpreadSheet(String token, String name, int rows,int columns) {
		this.columns=columns;
		this.rows=rows;
		this.name=name;
		this.token=token;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = BubbleDocs.getInstance();
			
		
		// Session validations and renewals
		validateUser(this.token);
		
		owner = bd.getUserByToken(token);
		sp = new SpreadSheet(owner, name, rows, columns);
		bd.addSpreadSheet(sp);
		sheetId = sp.getId();
				
		
	}

}
