package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exception.CellProtectedException;

public class Cell extends Cell_Base {
    
    public Cell() {
        super();
    }

	public void init(SpreadSheet SpreadSheet,int line, int column,String text) {
		setSpreadSheet(SpreadSheet);
		setLine(line);
		setColumn(column);	
		setText(text);
		setIsProtected(false);
	}
	
	@Override
	public void setText(String text) {
		if(getIsProtected())
			throw new CellProtectedException();
		super.setText(text);
	}

	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete() {
		setSpreadSheet(null);
		deleteDomainObject();
	}

	public Element export() {
		Element e = new Element("Cell");
		e.setAttribute("line", ""+getLine());
		e.setAttribute("column", ""+getColumn());
		e.setAttribute("text", getText());
		e.setAttribute("isProtected", ""+getIsProtected());
		return e;
	}
    
}
