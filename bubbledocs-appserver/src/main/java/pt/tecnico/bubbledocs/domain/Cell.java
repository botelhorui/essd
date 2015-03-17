package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exception.CellProtectedException;

public class Cell extends Cell_Base {
    
    public Cell() {
        super();
    }
    
    public Cell(SpreadSheet spread, int line, int column){
    	super();
    	init(spread, line, column);
    }

	protected void init(SpreadSheet spread, int line, int column){
		setSpreadSheet(spread);
		setLine(line);
		setColumn(column);
	}
	
	public void setLiteralContent(int value){
		LiteralContent lit = new LiteralContent();
		setContent(lit);
	}
	
	public void setReferenceContent(Cell targetCell){
		ReferenceContent ref = new ReferenceContent(targetCell);
		setContent(ref);
	}
	
	public void setBFAdd(Argument left, Argument right){
		BFAdd bf = new BFAdd(left, right);
		setContent(bf);
	}
	
	public void setBFSub(Argument left, Argument right){
		BFSub bf = new BFSub(left, right);
		setContent(bf);
	}
	
	public void setBFDiv(Argument left, Argument right){
		BFDiv bf = new BFDiv(left, right);
		setContent(bf);
	}
	
	public void setBFMul(Argument left, Argument right){
		BFMul bf = new BFMul(left, right);
		setContent(bf);
	}
	
	public int getValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void delete() {
		//Delete Roles
		setSpreadSheet(null);
		setReferenceContent( (Cell) null);
		setReferenceArgument(null);
		setContent(null);
		
		//Delete Object
		deleteDomainObject();
	}

	public Element export() {
		Element e = new Element("Cell");
		e.setAttribute("line", "" + getLine());
		e.setAttribute("column", "" + getColumn());
		
		if(this.getContent() != null){
			e.addContent(this.getContent().export());
		}
		
		return e;
	}
    
}
