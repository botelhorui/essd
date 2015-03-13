package pt.tecnico.bubbledocs.domain;

import org.jdom2.Document;
import org.jdom2.Element;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import pt.tecnico.bubbledocs.exception.CellProtectedException;
import pt.tecnico.bubbledocs.exception.PositionOutOfBoundsException;
import pt.tecnico.bubbledocs.exception.UserHasNotAccessException;
import pt.tecnico.bubbledocs.exception.UserHasNotWriteAccessException;

public class SheetData extends SheetData_Base{

	public SheetData() {
		super();
	}

	public void init(User creator, String name, int lines, int columns) {		
		setName(name);
		setLines(lines);
		setColumns(columns);
		setCreationDate(new DateTime());
		setId(BubbleDocs.getInstance().generateId());

		BubbleDocs.getInstance().addSheetData(this);
		setBubbleDocs(BubbleDocs.getInstance());

		setCreator(creator);
		creator.addCreatedSheet(this);

		SheetAccess sa = new SheetAccess();
		sa.init(creator,this,true);
		getSheetAccessSet().add(sa);
		creator.addSheetAccess(sa);
		creator.addSheetData(this);
	}

	private void checkBounds(int line,int column) throws PositionOutOfBoundsException{
		if(line < 1 || line > getLines() || column < 1 || column > getColumns())
			throw new PositionOutOfBoundsException();
	}

	private Cell getCell(int line, int column)throws PositionOutOfBoundsException{
		checkBounds(line, column);
		for(Cell x:getCellSet()){
			if(x.getLine() == line && x.getColumn() == column){
				return x;
			}
		}
		return null;
	}

	private void checkWriteAccess(String username) throws UserHasNotWriteAccessException,UserHasNotAccessException{
		checkReadAccess(username);
		SheetAccess sa = getSheetAccessSet().stream().filter(x -> x.getUser().getUsername().equals(username)).findFirst().get();
		if(!sa.getCanWrite())
			throw new UserHasNotWriteAccessException();
	}

	private void checkReadAccess(String username) throws UserHasNotAccessException{
		if(!getSheetAccessSet().stream().anyMatch(sa -> sa.getUser().getUsername().equals(username))){
			throw new UserHasNotAccessException();
		}
	}
	
	public String getValue(String username,int line, int column)
			throws PositionOutOfBoundsException, UserHasNotAccessException {
		checkReadAccess(username);
		Cell c = getCell(line,column);
		if(c==null){			
			return "";
		}
		return c.getValue();
	}

	public void setCellText(String username,int line, int column, String text)
			throws PositionOutOfBoundsException,
			CellProtectedException,
			UserHasNotWriteAccessException,
			UserHasNotAccessException{
		checkWriteAccess(username);
		Cell c = getCell(line,column);		
		if(c == null){
			c = new Cell();
			c.init(this,line,column,text);
			addCell(c);			
		}else{
			c.setText(text);
		}

	}

	public String getCellText(String username,int line, int column)
			throws PositionOutOfBoundsException {
		checkReadAccess(username);
		Cell c = getCell(line,column);
		if(c==null){
			return "";
		}
		return c.getText();
	}

	public void setUserAccess(String settingUser, String userToSet, boolean canWrite)
			throws UserHasNotWriteAccessException,
			UserHasNotAccessException{
		checkWriteAccess(settingUser);
		// TODO Auto-generated method stub

	}

	public Document export() {
		Document doc = new Document();
		Element root = new Element("SheetData");
		doc.setRootElement(root);

		root.setAttribute("name", getName());		
		root.setAttribute("creation-date",getCreationDate().toString(ISODateTimeFormat.dateTime()));
		root.setAttribute("lines", ""+getLines());
		root.setAttribute("columns", ""+getColumns());

		Element creator = new Element("Creator");
		creator.setAttribute("username", getCreator().getUsername());
		root.addContent(creator);

		Element cells = new Element("Cells");
		root.addContent(cells);
		for(Cell c: getCellSet()){
			cells.addContent(c.export());
		}

		return doc;
	}

	public void delete() {
		// SheetAccess delete is always called before this
		for(SheetAccess sa: getSheetAccessSet())
			sa.delete();
		for(Cell c: getCellSet())
			c.delete();
		for(User u: getUserSet())
			removeUser(u);
		setCreator(null);
		setBubbleDocs(null);
		deleteDomainObject();		
	}

}
