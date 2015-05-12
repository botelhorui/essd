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

public class Ticket {
	private static String FIELD_1 = "username";
	private static String FIELD_2 = "servicename";
	private static String FIELD_3 = "startTime";
	private static String FIELD_4 = "endTime";
	private static String FIELD_5 = "sessionKey";
	
	private String username;
	private String servicename;
	private String startTime;
	private String endTime;
	private byte[] sessionKey;

	
	public Ticket(String username, String servicename, String startTime,
			String endTime, byte[] sessionKey) {
		super();
		this.username = username;
		this.servicename = servicename;
		this.startTime = startTime;
		this.endTime = endTime;
		this.sessionKey = sessionKey;
	}

	public static void main(String[] args) {
		Ticket rar = new Ticket(
				"rui",
				"dropbox",
				"hoje",
				"amanha",
				"chave".getBytes());
		
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
		f2.setText(servicename);
		root.addContent(f2);
		
		Element f3 = new Element(FIELD_3);
		f3.setText(startTime);
		root.addContent(f3);
		
		Element f4 = new Element(FIELD_4);
		f4.setText(endTime);
		root.addContent(f4);
		
		Element f5 = new Element(FIELD_5);
		f5.setText(printBase64Binary(sessionKey));
		root.addContent(f5);
		
		XMLOutputter xml = new XMLOutputter();		
		
		return xml.outputString(doc).getBytes();
	}
	
	public static Ticket parse(byte[] bXML) throws JDOMException, IOException{
		SAXBuilder builder = new SAXBuilder();
		InputStream stream = new ByteArrayInputStream(bXML);
		Document doc = builder.build(stream);
		Element root = doc.getRootElement();
		return new Ticket(
				root.getChildText(FIELD_1),
				root.getChildText(FIELD_2),
				root.getChildText(FIELD_3),
				root.getChildText(FIELD_4),
				parseBase64Binary(root.getChildText(FIELD_5)));
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getServicename() {
		return servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public byte[] getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(byte[] sessionKey) {
		this.sessionKey = sessionKey;
	}
	
	


	
}
