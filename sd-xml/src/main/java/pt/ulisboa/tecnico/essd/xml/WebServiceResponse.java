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

public class WebServiceResponse {
	private static String FIELD_1 = "encryptedRequestTime";

	private byte[] encryptedRequestTime;

	public WebServiceResponse(byte[] encryptedRequestTime) {
		super();
		this.encryptedRequestTime = encryptedRequestTime;
	}

	public static void main(String[] args) throws JDOMException, IOException {
		WebServiceResponse rar = new WebServiceResponse(
				"lol".getBytes());
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
		f1.setText(printBase64Binary(encryptedRequestTime));
		root.addContent(f1);
		

		XMLOutputter xml = new XMLOutputter();		
		
		return xml.outputString(doc).getBytes();
	}
	
	public static WebServiceResponse parse(byte[] bXML) throws JDOMException, IOException{
		SAXBuilder builder = new SAXBuilder();
		InputStream stream = new ByteArrayInputStream(bXML);
		Document doc = builder.build(stream);
		Element root = doc.getRootElement();
		return new WebServiceResponse(
				parseBase64Binary(root.getChildText(FIELD_1)));
	}
	
	public byte[] getEncryptedRequestTime() {
		return encryptedRequestTime;
	}

	public void setEncryptedRequestTime(byte[] encryptedRequestTime) {
		this.encryptedRequestTime = encryptedRequestTime;
	}

}
