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
	
	public void setBinaryFunctionContent(Argument left, Argument right){
		BinaryFunction bf = new BinaryFunction(left, right);
		setContent(bf);
	}
	
	public int getValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void delete() {
		setSpreadSheet(null);
		deleteDomainObject();
	}

	public Element export() {
		//TODO by John
		return null;
	}
    
}
