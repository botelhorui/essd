package pt.tecnico.bubbledocs.domain;

import org.jdom2.Document;
import org.jdom2.Element;
import org.joda.time.DateTime;

import pt.tecnico.bubbledocs.exception.CellProtectedException;
import pt.tecnico.bubbledocs.exception.PositionOutOfBoundsException;
import pt.tecnico.bubbledocs.exception.UserHasNotWriteAccessException;

public class SheetData extends SheetData_Base implements Sheet{
    
    public SheetData() {
        super();
    }
    
	public void init(User creator, String name, int lines, int columns) {
		setCreator(creator);
		setName(name);
		setLines(lines);
		setColumns(columns);
		setCreationDate(new DateTime());
		setId(BubbleDocs.getInstance().generateId());	
	}
    
    private void checkBounds(int line,int column) throws PositionOutOfBoundsException{
    	if(line < 1 || line > getLines() || column < 1 || column > getColumns())
    		throw new PositionOutOfBoundsException();
    }
    
    public Cell getCell(int line, int column)throws PositionOutOfBoundsException{
    	checkBounds(line, column);
    	for(Cell x:getCellSet()){
    		if(x.getLine() == line && x.getColumn() == column){
				return x;
			}
    	}
    	return null;
    }

	@Override
	public String getValue(int line, int column)
			throws PositionOutOfBoundsException {
		Cell c = getCell(line,column);
		if(c==null){
			//if the cell is empty is not in the CellSet yet
			return "";
		}
		return c.getValue();
	}

	@Override
	public void setCell(int line, int column, String text)
			throws PositionOutOfBoundsException, CellProtectedException,
			UserHasNotWriteAccessException {
		Cell c = getCell(line,column);		
		if(c == null){
			c = new Cell();
			c.init(this,line,column,text);
			addCell(c);			
		}else{
			c.setText(text);
		}
				
	}

	@Override
	public String getCellText(int line, int column)
			throws PositionOutOfBoundsException {
		Cell c = getCell(line,column);
		if(c==null){
			return "";
		}
		return c.getText();
	}

	@Override
	public void setUserAccess(User user, boolean canWrite)
			throws UserHasNotWriteAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Document export() {
		Document doc = new Document();
		Element root = new Element("SheetData");
		doc.setRootElement(root);
		
		root.setAttribute("name", getName());
		root.setAttribute("creation-date", getCreationDate().toString());
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

	@Override
	public void delete() {
		// SheetAccess delete is always called before this
		for(Cell c: getCellSet())
			c.delete();
		setCreator(null);
		deleteDomainObject();		
	}
    
}
