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
import org.jdom2.output.XMLOutputter;

public class WebServiceRequest {
	private static String FIELD_1 = "encryptedAuthenticator";
	private static String FIELD_2 = "encryptedTicket";
	
	private byte[] encryptedAuthenticator;
	private byte[] encryptedTicket;

	public WebServiceRequest(byte[] encryptedAuthenticator,
			byte[] encryptedTicket) {
		super();
		this.encryptedAuthenticator = encryptedAuthenticator;
		this.encryptedTicket = encryptedTicket;
	}

	public static void main(String[] args) throws JDOMException, IOException {
		WebServiceRequest rar = new WebServiceRequest(
				"lol".getBytes(),
				"chave".getBytes());
		byte[] b = rar.encode();
		System.out.println(new String(rar.encode()));
		rar = parse(b);
		System.out.println(new String(rar.encode()));
	}
	
	public byte[] encode(){
		Document doc = new Document();
		Element root = new Element(this.getClass().getSimpleName());
		doc.setRootElement(root);
		
		Element f1 = new Element(FIELD_1);
		f1.setText(printBase64Binary(encryptedAuthenticator));
		root.addContent(f1);
		
		Element f2 = new Element(FIELD_2);
		f2.setText(printBase64Binary(encryptedTicket));
		root.addContent(f2);
		
		XMLOutputter xml = new XMLOutputter();		
		
		return xml.outputString(doc).getBytes();
	}
	
	public static WebServiceRequest parse(byte[] bXML) throws JDOMException, IOException{
		SAXBuilder builder = new SAXBuilder();
		InputStream stream = new ByteArrayInputStream(bXML);
		Document doc = builder.build(stream);
		Element root = doc.getRootElement();
		return new WebServiceRequest(
				parseBase64Binary(root.getChildText(FIELD_1)),
				parseBase64Binary(root.getChildText(FIELD_2)));
	}

}
