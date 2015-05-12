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

public class Authenticator {
	private static String FIELD_1 = "username";
	private static String FIELD_2 = "requestTime";
	
	private String username;
	private String requestTime;
	
	public Authenticator(String username, String requestTime) {
		super();
		this.username = username;
		this.requestTime = requestTime;
	}	
	
	public static void main(String[] args) throws JDOMException, IOException {
		Authenticator rar = new Authenticator("lol","adeus");
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
		f1.setText(username);
		root.addContent(f1);
		
		Element f2 = new Element(FIELD_2);
		f2.setText(requestTime);
		root.addContent(f2);
		
		XMLOutputter xml = new XMLOutputter();		
		
		return xml.outputString(doc).getBytes();
	}
	
	public static Authenticator parse(byte[] bXML) throws JDOMException, IOException{
		SAXBuilder builder = new SAXBuilder();
		InputStream stream = new ByteArrayInputStream(bXML);
		Document doc = builder.build(stream);
		Element root = doc.getRootElement();
		return new Authenticator(root.getChildText(FIELD_1),root.getChildText(FIELD_2));
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	
}
