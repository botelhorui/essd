package pt.tecnico.bubbledocs.domain;

import org.jdom2.Document;

import pt.tecnico.bubbledocs.exception.CellProtectedException;
import pt.tecnico.bubbledocs.exception.PositionOutOfBoundsException;
import pt.tecnico.bubbledocs.exception.UserHasNotWriteAccessException;

public interface Sheet {
	String getValue(int line,int column) throws PositionOutOfBoundsException;
	void setCell(int line,int column,String text) throws PositionOutOfBoundsException, CellProtectedException, UserHasNotWriteAccessException;
	String getCellText(int line,int column) throws PositionOutOfBoundsException;
	void setUserAccess(User user, boolean canWrite) throws UserHasNotWriteAccessException;
	Document export();
	void delete();
}
