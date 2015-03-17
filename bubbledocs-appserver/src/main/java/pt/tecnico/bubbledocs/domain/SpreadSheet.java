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
		

		BubbleDocs.getInstance().addSpreadSheet(this);
		setBubbleDocs(BubbleDocs.getInstance());

		setOwner(owner);
		owner.addOwnedSpread(this);

		SheetAccess sa = new SheetAccess();
		sa.init(owner,this,true);
		getSheetAccessSet().add(sa);
		owner.addSheetAccess(sa);
		owner.addOwnedSpread(this);
	}

	private void checkBounds(int line,int column) throws PositionOutOfBoundsException{
		if(line < 1 || line > getLines() || column < 1 || column > getColumns())
			throw new PositionOutOfBoundsException();
	}

	private Cell getCell(int line, int column) throws PositionOutOfBoundsException{
		checkBounds(line, column);
		for(Cell x:getCellSet()){
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

	/*public void setCellText(String username,int line, int column, String text)
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

	}*/

	/*public String getCellText(String username,int line, int column)
			throws PositionOutOfBoundsException {
		checkReadAccess(username);
		Cell c = getCell(line,column);
		if(c==null){
			return "";
		}
		return c.getText();
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
		//SheetAccess delete is always called before this
		for(SheetAccess sa: getSheetAccessSet())
			sa.delete();
		
		for(Cell c: getCellSet())
			c.delete();
		
		for(User r: getReaderUserSet())
			removeReaderUser(r);
		
		//TODO Writer
		
		setOwner(null);
		setBubbleDocs(null);
		deleteDomainObject();		
	}

}
