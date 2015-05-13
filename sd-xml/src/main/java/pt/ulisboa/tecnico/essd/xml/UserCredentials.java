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

public class UserCredentials {
	private static String FIELD_1 = "sessionKey";
	private static String FIELD_2 = "encryptionKey";
	private static String FIELD_3 = "nounce";
	
	private byte[] sessionKey;
	private byte[] encryptionKey;
	private int nounce;

	public UserCredentials(byte[] sk, byte[] ek, int n){
		this.sessionKey=sk;
		this.encryptionKey=ek;
		this.nounce=n;
	}
	
	public static void main(String[] args) {
		UserCredentials rar = new UserCredentials("ola".getBytes(),"ate ja".getBytes(), 747);
		System.out.println(new String(rar.encode()));
	}
	
	public byte[] encode(){
		Document doc = new Document();
		Element root = new Element(this.getClass().getSimpleName());
		doc.setRootElement(root);
		
		Element f1 = new Element(FIELD_1);
		f1.setText(printBase64Binary(sessionKey));
		root.addContent(f1);
		
		Element f2 = new Element(FIELD_2);
		f2.setText(printBase64Binary(encryptionKey));
		root.addContent(f2);
		
		Element f3 = new Element(FIELD_3);
		f3.setText(Integer.toString(nounce));
		root.addContent(f3);
		
		XMLOutputter xml = new XMLOutputter();		
		
		return xml.outputString(doc).getBytes();
	}
	
	public static UserCredentials parse(byte[] bXML) throws JDOMException, IOException{
		SAXBuilder builder = new SAXBuilder();
		InputStream stream = new ByteArrayInputStream(bXML);
		Document doc = builder.build(stream);
		Element root = doc.getRootElement();
		Element f1 = root.getChild(FIELD_1);
		Element f2 = root.getChild(FIELD_2);
		Element f3 = root.getChild(FIELD_3);
		byte[] bf1 = parseBase64Binary(f1.getText());
		byte[] bf2 = parseBase64Binary(f2.getText());
		int bf3 = 0;
		
		try{
			bf3 = Integer.parseInt(f3.getText());
		} catch (NumberFormatException e){
			System.out.println(e.getMessage());
		}
		
		return new UserCredentials(bf1,bf2,bf3);
	}

	public byte[] getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(byte[] sessionKey) {
		this.sessionKey = sessionKey;
	}

	public byte[] getEncryptionKey() {
		return encryptionKey;
	}

	public void setEncryptionKey(byte[] encryptionKey) {
		this.encryptionKey = encryptionKey;
	}

	public int getNounce() {
		return nounce;
	}

	public void setNounce(int nounce) {
		this.nounce = nounce;
	}


	
}
