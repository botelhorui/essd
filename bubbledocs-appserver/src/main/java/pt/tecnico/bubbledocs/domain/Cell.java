package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exception.importXMLException;

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
		LiteralContent lit = new LiteralContent(value);
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
		
		if(getContent() != null){
			getContent().delete();
			setContent(null);
		}
		
		//Delete Object
		deleteDomainObject();
	}

	public Element export() {
		Element e = new Element("Cell");
		e.setAttribute("line", "" + getLine());
		e.setAttribute("column", "" + getColumn());
		e.addContent(this.getContent().export());
		return e;
	}
	
	public Argument importArgumentXML(Element e){
		if(e.getName().equals("Literal")){
			return new LiteralArgument(Integer.parseInt(e.getAttributeValue("value")));
		} else if (e.getName().equals("Reference")){
			Element c = e.getChild("Cell");
			Cell cell = this.getSpreadSheet().getCell(Integer.parseInt(c.getAttributeValue("line")), Integer.parseInt(c.getAttributeValue("column")));
			return new ReferenceArgument(cell);
		} else {
			throw new importXMLException();
		}
	}
	
	public void importXML(Element e) throws NullPointerException{
		
		if(!e.getChildren().isEmpty()){
			Element content = e.getChildren().get(0);
			
			if (content.getName().equals("Literal")){
				this.setLiteralContent(Integer.parseInt(content.getAttributeValue("value")));
			}
			else if(content.getName().equals("Reference")){
				Element c = content.getChild("Cell");
				Cell cell = this.getSpreadSheet().getCell(Integer.parseInt(c.getAttributeValue("line")), Integer.parseInt(c.getAttributeValue("column")));
				this.setReferenceContent(cell);
			}
			else if(content.getName().startsWith("BF")){
				Element left = content.getChildren().get(0);
				Element right = content.getChildren().get(1);
				Argument leftArgument = importArgumentXML(left);
				Argument rightArgument = importArgumentXML(right);			
				if(content.getName().equals("BFADD")){	
					this.setBFAdd(leftArgument, rightArgument);			
				}			
				else if(content.getName().equals("BFSUB")){			
					this.setBFSub(leftArgument, rightArgument);				
				}			
				else if(content.getName().equals("BFMUL")){
					this.setBFMul(leftArgument, rightArgument);				
				}			
				else if(content.getName().equals("BFDIV")){
					this.setBFDiv(leftArgument, rightArgument);					
				}
			}else{
				throw new importXMLException();
			}
		}
		
	}
    
}
