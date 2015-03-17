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

		for(ReferenceContent rc : getReferenceContentSet()){
			removeReferenceContent(rc);
		}
		
		for(ReferenceArgument ra : getReferenceArgumentSet()){
			removeReferenceArgument(ra);
		}
		
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
	
	public void importXML(Element e) throws NullPointerException{
		
		if(!e.getChildren().isEmpty()){
			Element content = e.getChildren().get(0);
			
			if (content.getName() == "Literal"){
				this.setLiteralContent(Integer.parseInt(content.getAttributeValue("value")));
			}
			else if(content.getName() == "Reference"){
				Element c = content.getChild("Cell");
				Cell cell = this.getSpreadSheet().getCell(Integer.parseInt(c.getAttributeValue("line")), Integer.parseInt(c.getAttributeValue("column")));
				this.setReferenceContent(cell);
			}
			else if(content.getName() == "ADD"){
				Element left = content.getChildren().get(0);
				Element right = content.getChildren().get(1);
				Argument leftArgument = null;
				Argument rightArgument = null;
				if(left.getName() == "Literal"){
					leftArgument = new LiteralArgument(Integer.parseInt(left.getAttributeValue("value")));
				} else if (left.getName() == "Reference"){
					Element c = left.getChild("Cell");
					Cell cell = this.getSpreadSheet().getCell(Integer.parseInt(c.getAttributeValue("line")), Integer.parseInt(c.getAttributeValue("column")));
					leftArgument = new ReferenceArgument(cell);
				} else {
					return;
				}
				
				if(right.getName() == "Literal"){
					rightArgument = new LiteralArgument(Integer.parseInt(right.getAttributeValue("value")));
				} else if (right.getName() == "Reference"){
					Element c = right.getChild("Cell");
					Cell cell = this.getSpreadSheet().getCell(Integer.parseInt(c.getAttributeValue("line")), Integer.parseInt(c.getAttributeValue("column")));
					rightArgument = new ReferenceArgument(cell);
				} else {
					return;
				}
				
				this.setBFAdd(leftArgument, rightArgument);
				
			}
			
			else if(content.getName() == "SUB"){
				Element left = content.getChildren().get(0);
				Element right = content.getChildren().get(1);
				Argument leftArgument = null;
				Argument rightArgument = null;
				if(left.getName() == "Literal"){
					leftArgument = new LiteralArgument(Integer.parseInt(left.getAttributeValue("value")));
				} else if (left.getName() == "Reference"){
					Element c = left.getChild("Cell");
					Cell cell = this.getSpreadSheet().getCell(Integer.parseInt(c.getAttributeValue("line")), Integer.parseInt(c.getAttributeValue("column")));
					leftArgument = new ReferenceArgument(cell);
				} else {
					return;
				}
				
				if(right.getName() == "Literal"){
					rightArgument = new LiteralArgument(Integer.parseInt(right.getAttributeValue("value")));
				} else if (right.getName() == "Reference"){
					Element c = right.getChild("Cell");
					Cell cell = this.getSpreadSheet().getCell(Integer.parseInt(c.getAttributeValue("line")), Integer.parseInt(c.getAttributeValue("column")));
					rightArgument = new ReferenceArgument(cell);
				} else {
					return;
				}
				
				this.setBFSub(leftArgument, rightArgument);
				
			}
			
			else if(content.getName() == "MUL"){
				Element left = content.getChildren().get(0);
				Element right = content.getChildren().get(1);
				Argument leftArgument = null;
				Argument rightArgument = null;
				if(left.getName() == "Literal"){
					leftArgument = new LiteralArgument(Integer.parseInt(left.getAttributeValue("value")));
				} else if (left.getName() == "Reference"){
					Element c = left.getChild("Cell");
					Cell cell = this.getSpreadSheet().getCell(Integer.parseInt(c.getAttributeValue("line")), Integer.parseInt(c.getAttributeValue("column")));
					leftArgument = new ReferenceArgument(cell);
				} else {
					return;
				}
				
				if(right.getName() == "Literal"){
					rightArgument = new LiteralArgument(Integer.parseInt(right.getAttributeValue("value")));
				} else if (right.getName() == "Reference"){
					Element c = right.getChild("Cell");
					Cell cell = this.getSpreadSheet().getCell(Integer.parseInt(c.getAttributeValue("line")), Integer.parseInt(c.getAttributeValue("column")));
					rightArgument = new ReferenceArgument(cell);
				} else {
					return;
				}
				
				this.setBFMul(leftArgument, rightArgument);
				
			}
			
			else if(content.getName() == "DIV"){
				Element left = content.getChildren().get(0);
				Element right = content.getChildren().get(1);
				Argument leftArgument = null;
				Argument rightArgument = null;
				if(left.getName() == "Literal"){
					leftArgument = new LiteralArgument(Integer.parseInt(left.getAttributeValue("value")));
				} else if (left.getName() == "Reference"){
					Element c = left.getChild("Cell");
					Cell cell = this.getSpreadSheet().getCell(Integer.parseInt(c.getAttributeValue("line")), Integer.parseInt(c.getAttributeValue("column")));
					leftArgument = new ReferenceArgument(cell);
				} else {
					return;
				}
				
				if(right.getName() == "Literal"){
					rightArgument = new LiteralArgument(Integer.parseInt(right.getAttributeValue("value")));
				} else if (right.getName() == "Reference"){
					Element c = right.getChild("Cell");
					Cell cell = this.getSpreadSheet().getCell(Integer.parseInt(c.getAttributeValue("line")), Integer.parseInt(c.getAttributeValue("column")));
					rightArgument = new ReferenceArgument(cell);
				} else {
					return;
				}
				
				this.setBFDiv(leftArgument, rightArgument);
				
			}
	
		}
		
	}
    
}
