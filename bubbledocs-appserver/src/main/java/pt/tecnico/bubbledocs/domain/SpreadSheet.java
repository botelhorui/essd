package pt.tecnico.bubbledocs.domain;

import org.jdom2.Document;
import org.jdom2.Element;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import pt.tecnico.bubbledocs.exception.CellProtectedException;
import pt.tecnico.bubbledocs.exception.PositionOutOfBoundsException;
import pt.tecnico.bubbledocs.exception.UserHasNotAccessException;
import pt.tecnico.bubbledocs.exception.UserHasNotWriteAccessException;

public class SpreadSheet extends SpreadSheet_Base{

	public SpreadSheet() {
		super();
	}
	
	public SpreadSheet(User owner, String name, int lines, int columns){
		super();
		init(owner, name, lines, columns);
	}

	protected void init(User owner, String name, int lines, int columns) {		
		setId(BubbleDocs.getInstance().generateId());
		setName(name);
		setCreationDate(new DateTime());
		setLines(lines);
		setColumns(columns);
		
		//Create all the spread's empty cells
		for(int i=1; i <= lines ;i++){
    		for(int j=1; j <= columns ;j++){
    			Cell c = new Cell(this, i, j);
    		}
    	}
		

		BubbleDocs.getInstance().addSpreadSheet(this);
		setBubbleDocs(BubbleDocs.getInstance());

		setOwner(owner);
		owner.addOwnedSpread(this);
		addReaderUser(owner);
		owner.addReadableSpread(this);
		addWriterUser(owner);
		owner.addWritableSpread(this);

		SheetAccess sa = new SheetAccess(owner, this);
		setSheetAccess(sa);
		owner.addSheetAccess(sa);
		
	}
	
	public Cell getCell(int line, int column) throws PositionOutOfBoundsException{
		if(line < 1 || line > getLines() || column < 1 || column > getColumns())
			throw new PositionOutOfBoundsException();
		
		for(Cell x : getCellSet()){
			if(x.getLine() == line && x.getColumn() == column){
				return x;
			}
		}
		return null;
	}

	private void checkWriteAccess(String username) throws UserHasNotWriteAccessException,UserHasNotAccessException{
		//checkReadAccess(username);
		//SheetAccess sa = getSheetAccessSet().stream().filter(x -> x.getUser().getUsername().equals(username)).findFirst().get();
	}

	private void checkReadAccess(String username) throws UserHasNotAccessException{
		//if(!getSheetAccessSet().stream().anyMatch(sa -> sa.getUser().getUsername().equals(username))){
			//throw new UserHasNotAccessException();
		//}
	}
	
	/*public String getValue(String username,int line, int column)
			throws PositionOutOfBoundsException, UserHasNotAccessException {
		checkReadAccess(username);
		Cell c = getCell(line,column);
		if(c==null){			
			return "";
		}
		return c.getValue();
	}*/

	/*public void setUserAccess(String settingUser, String userToSet, boolean canWrite)
			throws UserHasNotWriteAccessException,
			UserHasNotAccessException{
		checkWriteAccess(settingUser);
		// TODO Auto-generated method stub

	}*/

	public Document export() {
		Document doc = new Document();
		Element root = new Element("SpreadSheet");
		doc.setRootElement(root);

		root.setAttribute("name", getName());		
		root.setAttribute("creation-date",getCreationDate().toString(ISODateTimeFormat.dateTime()));
		root.setAttribute("lines", ""+getLines());
		root.setAttribute("columns", ""+getColumns());

		Element owner = new Element("Owner");
		owner.setAttribute("username", getOwner().getUsername());
		root.addContent(owner);
		
		Element readers = new Element("Readers");
		root.addContent(readers);
		for(User u : getReaderUserSet()){
			Element user = new Element("User");
			user.setAttribute("username", u.getUsername());
			readers.addContent(user);
		}
		
		Element writers = new Element("Writers");
		root.addContent(writers);
		for(User u : getWriterUserSet()){
			Element user = new Element("User");
			user.setAttribute("username", u.getUsername());
			writers.addContent(user);
		}

		Element cells = new Element("Cells");
		
		root.addContent(cells);
		for(Cell c: getCellSet()){
			cells.addContent(c.export());
		}
		
		return doc;
	}

	public void delete() {
		//Delete Roles
		getSheetAccess().delete();
		
		for(Cell c: getCellSet()){
			c.delete();
		}
		
		for(User r: getReaderUserSet()){
			removeReaderUser(r);
		}
		
		for(User w: getWriterUserSet()){
			removeWriterUser(w);
		}
		
		setOwner(null);
		setBubbleDocs(null);
		
		//Delete Object
		deleteDomainObject();		
	}

}
