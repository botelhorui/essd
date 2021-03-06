package pt.tecnico.bubbledocs.domain;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;
import java.util.StringTokenizer;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.PositionOutOfBoundsException;

public class SpreadSheet extends SpreadSheet_Base{

	public SpreadSheet() {
		super();
	}
	
	public SpreadSheet(User owner, String name, int lines, int columns){
		super();
		init(owner, name, lines, columns);
	}

	protected void init(User owner, String name, int lines, int columns) throws PositionOutOfBoundsException {
		
		if ((lines < 1) || (columns < 1)){
			throw new PositionOutOfBoundsException();
		}
		
		setId(BubbleDocs.getInstance().generateId());
		setName(name);
		setCreationDate(new DateTime());
		setLines(lines);
		setColumns(columns);
		
		//Create all the spread's empty cells
		for(int i=1; i <= lines ;i++){
    		for(int j=1; j <= columns ;j++){
				new Cell(this, i, j);
    		}
    	}

		BubbleDocs.getInstance().addSpreadSheet(this);
		setBubbleDocs(BubbleDocs.getInstance());

		setOwner(owner);
		owner.addOwnedSpread(this);
		addReader(owner);
		addWriter(owner);

	}
	
	public void addReader(User u){
		addReaderUser(u);
		u.addReadableSpread(this);		
	}
	
	public void addWriter(User u){
		addWriterUser(u);
		u.addWritableSpread(this);
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
			if(c.getContent() != null){
				cells.addContent(c.export());
			}
		}
		
		return doc;
	}

	public void delete() {
		//Delete Roles
		
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
	
	public boolean checkReadAccess(User user){
		for(User r: getReaderUserSet()){
			if(user.equals(r))
				return true;
		}
		
		return false;
	}

	public boolean checkWriteAccess(User user){
		for(User w: getWriterUserSet()){
			if(user.equals(w))
				return true;
		}
		
		return false;
	}

	//Overloaded -- could not Override -- some Fenix Framework class has equals as final
	public boolean equals(SpreadSheet that) {
		// Custom equality check here.
		
		if(this.getName().equals(that.getName()))
				if(this.getId() == that.getId()
				&& this.getLines() == that.getLines()
				&& this.getColumns() == that.getColumns())
					return true;
		
		return false;
	}
	
	public Cell getCellFromString(String input) throws PositionOutOfBoundsException{
		
		StringTokenizer st = new StringTokenizer(input, ";", false);
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

		Cell cell = getCell(cellRow, cellColumn);
		
		return cell;
		
	}
	
	
	public byte[] spreadtoBytes() throws IOException {
		
		XMLOutputter xml = new XMLOutputter();
        
        	
        	Document jdomDoc = this.export();
        	
            byte[] bytes = xml.outputString(jdomDoc).getBytes("UTF-8");
       
            return bytes;
		
	}
	
	
	
	
	
}