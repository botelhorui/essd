package pt.ulisboa.tecnico.essd.xml;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import java.io.IOException;

public class ReservedXML {
	
	private Document _xml;
	
	private String _serviceName;
	private int _nounce;
	
	private XMLOutputter xmlOutput;
	
	public ReservedXML (String serviceName, int nounce){
		
		_serviceName = serviceName;
		_nounce = nounce;
		
		xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		xmlOutput.getFormat().setOmitDeclaration(true);
		
	}
	
	public void generateXML(){
		
		Document doc = new Document();
		Element reserved = new Element("reserved");
		doc.setRootElement(reserved);
		
		Element sn = new Element("service-name");
		sn.setText(_serviceName);
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