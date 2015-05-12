package pt.ulisboa.tecnico.essd.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import java.io.IOException;

public class ReservedXML {
	
	private Document _xml;
	
	private String _service-name;
	private int _nounce;
	
	private XMLOutputter xmlOutput;
	
	public ReservedXML (String service-name, int nounce){
		
		_service-name = service-name;
		_nounce = nounce;
		
		xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		
	}
	
	public void generateXML(){
		
		Document doc = new Document();
		Element reserved = new Element("reserved");
		doc.setRootElement(reserved);
		
		Element sn = new Element("service-name");
		sn.setText(_service-name);
		reserved.addContent(sn);
		
		Element nounce = new Element("nounce");
		nounce.setText(Integer.toString(_nounce));
		reserved.addContent(nounce);
		
		_xml = doc;
		
	}
	
	public void toOutput(){
		
		try{
			
			xmlOutput.output(_xml, System.out);
			
		} catch (IOException io) {
			System.out.println(io.getMessage());
		}
		
	}
	
	
}