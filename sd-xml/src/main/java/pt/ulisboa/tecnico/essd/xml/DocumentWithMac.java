package pt.ulisboa.tecnico.essd.xml;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class DocumentWithMac {
	
	private Document _xml;

	private static String FIELD_1 = "document";
	private static String FIELD_2 = "digest";
	
	
	private byte[] _document, _digest;
	
	
	
	
	private XMLOutputter xmlOutput;
	
	
	
	public DocumentWithMac(byte[] _document, byte[] _digest) {
		
		this._document = _document;
		this._digest = _digest;
		
		
		
		xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		xmlOutput.getFormat().setOmitDeclaration(true);
		
	}



	public byte[] get_document() {
		return _document;
	}



	public void set_document(byte[] _document) {
		this._document = _document;
	}



	public byte[] get_digest() {
		return _digest;
	}



	public void set_digest(byte[] _digest) {
		this._digest = _digest;
	}
	
	private void generateXML(){
			
			Document doc = new Document();
			Element docmac = new Element("DocumentMac");
			doc.setRootElement(docmac);
			
			Element dc = new Element(FIELD_1);
			dc.setText(printBase64Binary(_document));
			docmac.addContent(dc);
			
			Element dg = new Element(FIELD_2);
			dg.setText(printBase64Binary(_digest));
			docmac.addContent(dg);
			
			_xml = doc;
			
		}
	
	public byte[] encode(){
		generateXML();
		return xmlOutput.outputString(_xml).getBytes();
	}
	
	public void toOutput(){
			
			try{
				
				xmlOutput.output(_xml, System.out);
				
			} catch (IOException io) {
				System.out.println(io.getMessage());
			}
			
		}
	
	public static DocumentWithMac parse(byte[] bXML) throws JDOMException, IOException{
			
			SAXBuilder builder = new SAXBuilder();
			InputStream stream = new ByteArrayInputStream(bXML);
			Document doc = builder.build(stream);
			Element root = doc.getRootElement();
			Element dc = root.getChild(FIELD_1);
			Element dg = root.getChild(FIELD_2);
		
			byte[] actual_dc = parseBase64Binary(dc.getText());
			byte[] actual_dg = parseBase64Binary(dg.getText());
			
			
			return new DocumentWithMac(actual_dc, actual_dg);
			
		}
	
	
}
