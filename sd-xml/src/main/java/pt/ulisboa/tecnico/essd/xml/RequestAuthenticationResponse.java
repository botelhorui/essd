package pt.ulisboa.tecnico.essd.xml;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import java.util.Arrays;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

public class RequestAuthenticationResponse {
	
	private static String FIELD_1 = "encryptedTicket";
	private static String FIELD_2 = "encryptedUserCredentials";
	
	private byte[] encryptedTicket;
	private byte[] encryptedUserCredentials;

	public RequestAuthenticationResponse(byte[] encryptedTicket, byte[] encryptedUserCredentials){
		this.encryptedTicket=encryptedTicket;
		this.encryptedUserCredentials=encryptedUserCredentials;
	}
	
	public static void main(String[] args) throws JDOMException, IOException {
		RequestAuthenticationResponse rar = new RequestAuthenticationResponse("ola".getBytes(),"adeus".getBytes());
		System.out.println(new String(rar.encode()));
		RequestAuthenticationResponse rol = RequestAuthenticationResponse.parse(rar.encode());
		if(Arrays.equals(rar.getEncryptedTicket(),rol.getEncryptedTicket())){
			System.out.println("YAY!");
		}
		if(Arrays.equals(rar.getEncryptedUserCredentials(), rol.getEncryptedUserCredentials())){
			System.out.println("WOOHOO!");
		}
	}
	
	public byte[] encode(){
		Document doc = new Document();
		Element root = new Element(this.getClass().getSimpleName());
		doc.setRootElement(root);
		
		Element et = new Element(FIELD_1);
		et.setText(printBase64Binary(encryptedTicket));
		root.addContent(et);
		
		Element euc = new Element(FIELD_2);
		euc.setText(printBase64Binary(encryptedUserCredentials));
		root.addContent(euc);
		
		XMLOutputter xml = new XMLOutputter();		
		
		return xml.outputString(doc).getBytes();
	}
	
	public static RequestAuthenticationResponse parse(byte[] bXML) throws JDOMException, IOException{
		SAXBuilder builder = new SAXBuilder();
		InputStream stream = new ByteArrayInputStream(bXML);
		Document doc = builder.build(stream);
		Element root = doc.getRootElement();
		Element et = root.getChild(FIELD_1);
		Element euc = root.getChild(FIELD_2);
		byte[] bet = parseBase64Binary(et.getText());
		byte[] beuc = parseBase64Binary(euc.getText());
		return new RequestAuthenticationResponse(bet,beuc);
	}

	public byte[] getEncryptedTicket() {
		return encryptedTicket;
	}

	public void setEncryptedTicket(byte[] encryptedTicket) {
		this.encryptedTicket = encryptedTicket;
	}

	public byte[] getEncryptedUserCredentials() {
		return encryptedUserCredentials;
	}

	public void setEncryptedUserCredentials(byte[] encrypterUserCredentials) {
		this.encryptedUserCredentials = encrypterUserCredentials;
	}
	
	
}
