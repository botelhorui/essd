package pt.ulisboa.tecnico.essd.xml;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

import java.lang.NumberFormatException;

public class ReservedXML {
	
	private Document _xml;
	
	private static final String FIELD_1 = "service-name";
	private static final String FIELD_2 = "nounce";
	
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
	
	public String getServiceName(){
		return _serviceName;
	}
	
	public int getNounce(){
		return _nounce;
	}
	
	public void setServiceName(String value){
		_serviceName = value;
	}
	
	public void setNounce(int value){
		_nounce = value;
	}
	
	private void generateXML(){
		
		Document doc = new Document();
		Element reserved = new Element("reserved");
		doc.setRootElement(reserved);
		
		Element sn = new Element(FIELD_1);
		sn.setText(_serviceName);
		reserved.addContent(sn);
		
		Element nounce = new Element(FIELD_2);
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
	
	
	public byte[] encode(){
		generateXML();
		return xmlOutput.outputString(_xml).getBytes();
	}
	
	
	public static ReservedXML parse(byte[] bXML) throws JDOMException, IOException{
		
		SAXBuilder builder = new SAXBuilder();
		InputStream stream = new ByteArrayInputStream(bXML);
		Document doc = builder.build(stream);
		Element root = doc.getRootElement();
		Element sn = root.getChild(FIELD_1);
		Element nounce = root.getChild(FIELD_2);
	
		String actual_sn = sn.getText();
		int actual_nounce = 0;
		try{
			actual_nounce = Integer.parseInt(nounce.getText());
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
		}
		
		return new ReservedXML(actual_sn, actual_nounce);
		
	}
	
	
}