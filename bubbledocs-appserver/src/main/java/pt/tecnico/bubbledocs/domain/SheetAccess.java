package pt.tecnico.bubbledocs.domain;

import org.jdom2.Document;

import pt.tecnico.bubbledocs.exception.CellProtectedException;
import pt.tecnico.bubbledocs.exception.PositionOutOfBoundsException;
import pt.tecnico.bubbledocs.exception.UserHasNotWriteAccessException;

public class SheetAccess extends SheetAccess_Base implements Sheet{
    
    public SheetAccess() {
        super();
    }

	public void delete() {
		SheetData s = getSheetData();
		setSheetData(null);	
		s.delete();		
		setUser(null);			
		deleteDomainObject();		
	}

	public void init(User user, SheetData sheetData, boolean canWrite) {
		setUser(user);
		setSheetData(sheetData);
		setCanWrite(canWrite);	
		user.addSheetAccess(this);
		sheetData.addSheetAccess(this);
	}

	@Override
	public String getValue(int line, int column)
			throws PositionOutOfBoundsException {
		return getSheetData().getValue(line, column);
	}

	@Override
	public void setCell(int line, int column, String text)
			throws PositionOutOfBoundsException, CellProtectedException,
			UserHasNotWriteAccessException {
		if(!getCanWrite())
			throw new UserHasNotWriteAccessException();
		getSheetData().setCell(line, column, text);
	}

	@Override
	public String getCellText(int line, int column)
			throws PositionOutOfBoundsException {
		return getSheetData().getCellText(line, column);
	}

	@Override
	public void setUserAccess(User user, boolean canWrite)
			throws UserHasNotWriteAccessException {
		getSheetData().setUserAccess(user,canWrite);
	}

	@Override
	public Document export() {
		return getSheetData().export();
	}
    
}
